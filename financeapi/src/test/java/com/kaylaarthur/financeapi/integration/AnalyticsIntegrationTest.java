package com.kaylaarthur.financeapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import javax.sql.DataSource;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AnalyticsIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    private String token;

    @BeforeEach
    void setup() throws Exception {
        try(Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Transactions");
            stmt.execute("DELETE FROM Budgets");
            stmt.execute("DELETE FROM Categories");
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

    private long makeAccount(String name, String type, double balance) throws Exception {
        String response = mockMvc.perform(post("/accounts")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "name": "%s",
                    "type": "%s",
                    "balance": "%f"
                }
            """.formatted(name, type, balance)))
            .andReturn()
            .getResponse()
            .getContentAsString();

            return ((Number) JsonPath.read(response, "$.accountId")).longValue();
    } // makeAccount


    private long makeCategory(String name) throws Exception {
        String response = mockMvc.perform(post("/categories")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "%s" }
            """.formatted(name)))
            .andReturn()
            .getResponse()
            .getContentAsString();

            return ((Number) JsonPath.read(response, "$.categoryId")).longValue();
    } // makeCategory

    private long makeTransaction(
        long categoryId, 
        long accountId, 
        double amount, 
        String type) throws Exception {
        
        String response = mockMvc.perform(post("/transactions")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            { 
                "categoryId": %d,
                "accountId": %d ,
                "amount": %f,
                "date": "2026-01-01",
                "description": "TESTING TESTING",
                "transactionType": "%s"
            }
        """.formatted(categoryId, accountId, amount, type)))
        .andReturn()
        .getResponse()
        .getContentAsString();

        return ((Number) JsonPath.read(response, "$.transactionId")).longValue();
    } // makeTransaction


    private long makeBudget(long categoryId, double limit, String period) throws Exception {
        String response = mockMvc.perform(post("/budgets")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            { 
                "categoryId": %d,
                "budgetLimit": %f,
                "period": "%s"
            }
        """.formatted(categoryId, limit, period)))
        .andReturn()
        .getResponse()
        .getContentAsString();

        return ((Number) JsonPath.read(response, "$.budgetId")).longValue();
    } // makeBudget

    @Test
    void shouldCalculateBudgetUsage() throws Exception {
        long categoryId = makeCategory("Food");
        long accountId = makeAccount("Test", "CHECKINGS", 1000);

        makeBudget(categoryId, 500.00, "MONTHLY");

        makeTransaction(categoryId, accountId, 100, "EXPENSE");
        makeTransaction(categoryId, accountId, 50, "EXPENSE");

        mockMvc.perform(get("/analytics/budget-usage")
            .param("categoryId", String.valueOf(categoryId))
            .param("period", "MONTHLY")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.limit").value(500.00))
            .andExpect(jsonPath("$.spent").value(150.00))
            .andExpect(jsonPath("$.remaining").value(350.00));
    } // 
} // AnalyticsIntegrationTest
