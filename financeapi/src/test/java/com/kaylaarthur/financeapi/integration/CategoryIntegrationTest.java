package com.kaylaarthur.financeapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
public class CategoryIntegrationTest {

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
            stmt.execute("DELETE FROM Categories");
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

    long makeCategoryAndGetId(String name) throws Exception {
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
    } // makeCategoryAndGetId

    @Test
    void shouldCreateCategory() throws Exception {
        mockMvc.perform(post("/categories")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Food" }
            """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.categoryName").value("Food"));
    } // shouldCreateCategory

    @Test
    void shouldRejectDuplicateCategory() throws Exception {
        mockMvc.perform(post("/categories")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Food" }
            """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.categoryName").value("Food"));

        mockMvc.perform(post("/categories")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "name": "Food" }
            """))
            .andExpect(status().isBadRequest());
    } // shouldRejectDUplicateCategory

    @Test
    void shouldGetAllCategories() throws Exception {
        makeCategoryAndGetId("Food");
        makeCategoryAndGetId("Car");

        mockMvc.perform(get("/categories")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    } // shouldGetAllCategories

    @Test
    void shouldGetCategoryById() throws Exception {
        long id = makeCategoryAndGetId("Food");

        mockMvc.perform(get("/categories/" + id)
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryName").value("Food"));
    } // shouldGetCategoryById

    @Test
    void shouldUpdateCategory() throws Exception {
        long id = makeCategoryAndGetId("Food");

        mockMvc.perform(put("/categories/" + id)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "categoryName": "Groceries" }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryName").value("Groceries"));
    } // shouldUpdateCategory

    @Test
    void shouldRejectUpdateWithDuplicateName() throws Exception {
        makeCategoryAndGetId("Food");
        long id = makeCategoryAndGetId("Car");

        mockMvc.perform(put("/categories/" + id)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "categoryName": "Food" }
            """))
            .andExpect(status().isBadRequest());
    } // shouldRejectUpdateWithDuplicateName

    @Test
    void shouldDeleteCategory() throws Exception {
        long id = makeCategoryAndGetId("Food");

        mockMvc.perform(delete("/categories/" + id)
            .header("Authorization", token))
            .andExpect(status().isNoContent());
    } // shouldDeleteCategory

    @Test
    void shouldReturn404WhenDeletingNonexistent() throws Exception {
        mockMvc.perform(delete("/categories/9")
            .header("Authorization", token))
            .andExpect(status().isBadRequest());
    } // shouldReturn404WhenDeletingNonexistent

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/categories"))
            .andExpect(status().isUnauthorized());
    } // shouldRejectRequestWithoutToken
    
    
} // CategoryIntegrationTest
