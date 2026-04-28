package com.kaylaarthur.financeapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.sql.Connection;
import java.sql.Statement;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.JsonPath;

import javax.sql.DataSource;;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountIntegration {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        try(Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Accounts");
            stmt.execute("DELETE FROM Users");
            
        } // try
        mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "name": "Testing100",
                            "email": "testing100@gmail.com",
                            "password": "testPass100!"
                        }
                    """))
                    .andExpect(status().isCreated());

        // login
        String loginResponse = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "email": "testing100@gmail.com",
                    "password": "testPass100!"
                }
            """))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        token = "Bearer " + JsonPath.read(loginResponse, "$.token");

    } // setup

   

    void createAccounts() throws Exception {
        // create account 1
        mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "name": "CheckingsTest",
                        "type": "CHECKINGS",
                        "balance": 1000.00
                    }
                """
            ))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("CheckingsTest"))
            .andExpect(jsonPath("$.balance").value(1000.00));


        // create account 2
        mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "name": "SavingsTest",
                        "type": "SAVINGS",
                        "balance": 307800.00
                    }
                """
            ))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("SavingsTest"))
            .andExpect(jsonPath("$.balance").value(307800.00));


        // create account 3
        mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "name": "CreditsTest",
                        "type": "CREDIT",
                        "balance": 50000.00
                    }
                """
            ))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("CreditsTest"))
            .andExpect(jsonPath("$.balance").value(50000.00));
    } // createAccounts

    long createOneAccount() throws Exception {
        String result = mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "name": "CheckingsTest",
                        "type": "CHECKINGS",
                        "balance": 1000.00
                    }
                """
            ))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
            return ((Number) JsonPath.read(result, "$.accountId")).longValue();
    }
    
    @Test
    void shouldCreateAccounts() throws Exception {
        // create account 1
        mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "name": "CheckingsTest",
                        "type": "CHECKINGS",
                        "balance": 1000.00
                    }
                """
            ))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("CheckingsTest"))
            .andExpect(jsonPath("$.balance").value(1000.00));


        // create account 2
        mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "name": "SavingsTest",
                        "type": "SAVINGS",
                        "balance": 307800.00
                    }
                """
            ))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("SavingsTest"))
            .andExpect(jsonPath("$.balance").value(307800.00));


        // create account 3
        mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                    {
                        "name": "CreditsTest",
                        "type": "CREDIT",
                        "balance": 50000.00
                    }
                """
            ))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("CreditsTest"))
            .andExpect(jsonPath("$.balance").value(50000.00));
    } // shouldCreateAccounts


    @Test
    void shouldGetAllAccounts() throws Exception {
        createAccounts();
        mockMvc.perform(get("/accounts")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3));
    } // shouldGetAllAccounts


    @Test
    void shouldGetAccountById() throws Exception {
        createAccounts();
        long id = 1;

        mockMvc.perform(get("/accounts/" + id)
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId").value(id))
            .andExpect(jsonPath("$.name").value("CheckingsTest"));
    } // shouldGetAccountById

    @Test
    void shouldUpdateAccount() throws Exception {
        long id = createOneAccount();
        
        mockMvc.perform(put("/accounts/" + id)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
               """ 
                    {
                        "name": "Updated Checkings",
                        "balance": 24.00
                    }
                """
            ))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Checkings"))
            .andExpect(jsonPath("$.type").value("CHECKINGS"))
            .andExpect(jsonPath("$.balance").value(24.00));
    } // shouldUpdateAccount

    @Test 
    void shouldDeleteAccount() throws Exception {
        long id = createOneAccount();

        mockMvc.perform(delete("/accounts/" + id)
            .header("Authorization", token))
            .andExpect(status().isNoContent());
    } // shouldDeleteAccount

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/accounts"))
            .andExpect(status().isUnauthorized());
    } // shouldRejectRequestWithoutToken


} // AccountIntegration
