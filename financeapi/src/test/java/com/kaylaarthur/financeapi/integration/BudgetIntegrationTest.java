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
public class BudgetIntegrationTest {
    
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
    void shouldCreateBudget() throws Exception {
        long categoryId = makeCategory("Food");

        mockMvc.perform(post("/budgets")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { 
                    "categoryId": %d,
                    "budgetLimit": 500.00,
                    "period": "MONTHLY"
                }
            """.formatted(categoryId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.budgetLimit").value(500.00));
    } // shouldCreateBudget

    @Test
    void shouldRejectDuplicateBudgetForSameCategoryAndPeriod() throws Exception {
        long categoryId = makeCategory("Food");

        makeBudget(categoryId, 500, "MONTHLY");

        mockMvc.perform(post("/budgets")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { 
                    "categoryId": %d,
                    "budgetLimit": 300.00,
                    "period": "MONTHLY"
                }
            """.formatted(categoryId)))
            .andExpect(status().isBadRequest());
    } // shouldRejectDuplicateBudgetForSameCategoryAndPeriod

    @Test
    void shouldGetBudgetById() throws Exception {
        long categoryId = makeCategory("Food");
        long budgetId = makeBudget(categoryId, 500, "MONTHLY");

        mockMvc.perform(get("/budgets/" + budgetId)
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.budgetId").value(budgetId));
    } // shouldGetBudgetById

    @Test
    void shouldGetAllBudgets() throws Exception {
        long categoryId = makeCategory("Food");
        long categoryId2 = makeCategory("Travel");

        makeBudget(categoryId, 500, "MONTHLY");
        makeBudget(categoryId2, 300, "MONTHLY");

        mockMvc.perform(get("/budgets")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    } // shouldGetAllBudgets

    @Test
    void shouldGetBudgetsByCategory() throws Exception {
        long categoryId = makeCategory("Food");
        long categoryId2 = makeCategory("Travel");

        makeBudget(categoryId, 500, "MONTHLY");
        makeBudget(categoryId2, 300, "MONTHLY");

        mockMvc.perform(get("/budgets")
            .param("categoryId", String.valueOf(categoryId))
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    } // shouldGetBudgetsByCategory

    @Test
    void shouldGetBudgetsByPeriod() throws Exception {
        long categoryId = makeCategory("Food");

        makeBudget(categoryId, 500, "MONTHLY");
        makeBudget(categoryId, 300, "YEARLY");

        mockMvc.perform(get("/budgets")
            .param("period", "MONTHLY")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    } // shouldGetBudgetsByPeriod

    @Test
    void shouldUpdateBudget() throws Exception {
        long categoryId = makeCategory("Food");
        long budgetId = makeBudget(categoryId, 500, "MONTHLY");

        mockMvc.perform(put("/budgets/" + budgetId)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "budgetLimit": 900.00
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.budgetLimit").value(900.00));
    } // shouldUpdateBudget

    @Test
    void shouldRejectUpdatesThatViolateUniqueness() throws Exception {
        long categoryId = makeCategory("Food");
        makeBudget(categoryId, 500, "MONTHLY");
        long budgetId = makeBudget(categoryId, 1000, "YEARLY");

        mockMvc.perform(put("/budgets/" + budgetId)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "period": "MONTHLY" }
            """))
            .andExpect(status().isBadRequest());
    } // shouldRejectUpdatesThatViolateUniqueness

    @Test
    void shouldDeleteBudget() throws Exception {
        long categoryId = makeCategory("Food");
        long budgetId = makeBudget(categoryId, 500, "MONTHLY");

        mockMvc.perform(delete("/budgets/" + budgetId)
            .header("Authorization", token))
            .andExpect(status().isNoContent());
    } // shouldDeleteBudget

    @Test
    void shouldRejectWithoutToken() throws Exception {
        long categoryId = makeCategory("Food");
        long budgetId = makeBudget(categoryId, 500, "MONTHLY");

        mockMvc.perform(put("/budgets/" + budgetId))
            .andExpect(status().isUnauthorized());
    } // shouldRejectWithoutToken

    @Test
    void shouldRejectDuplicateBudgetOnUpdate() throws Exception {
        long categoryId = makeCategory("Food");
        makeBudget(categoryId, 500.00, "MONTHLY");

        long budget2 = makeBudget(categoryId, 300.00, "YEARLY");

        mockMvc.perform(put("/budgets/" + budget2)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "period": "MONTHLY"
                }
            """))
            .andExpect(status().isBadRequest());
    } // shouldRejectDuplicateBudgetOnUpdate

    @Test
    void shouldAllowUpdatingSameBudgetWithoutConflict() throws Exception {
        long categoryId = makeCategory("Food");

        long budgetId = makeBudget(categoryId, 500.00, "MONTHLY");

        mockMvc.perform(put("/budgets/" + budgetId)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "budgetLimit": 800.00
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.budgetLimit").value(800.00));
    } // shouldAllowUpdatingSameBudgetWithoutConflict

    @Test
    void shouldRejectWhenUpdatingCategoryCausesDuplicate() throws Exception {
        long category1 = makeCategory("Food");
        long category2 = makeCategory("Travel");

        makeBudget(category1, 500.00, "MONTHLY");
        long budget2 = makeBudget(category2, 300.00, "MONTHLY");

        mockMvc.perform(put("/budgets/" + budget2)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d
                }
            """.formatted(category1)))
            .andExpect(status().isBadRequest());
    } // shouldRejectWhenUpdatingCategoryCausesDuplicate

@Test
    void shouldUpdateCategoryAndPeriodWhenNoConflict() throws Exception {
        long category1 = makeCategory("Food");
        long category2 = makeCategory("Travel");

        long budgetId = makeBudget(category1, 500.00, "MONTHLY");

        mockMvc.perform(put("/budgets/" + budgetId)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d,
                    "period": "YEARLY"
                }
            """.formatted(category2)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryId").value(category2))
            .andExpect(jsonPath("$.period").value("YEARLY"));
    } // shouldUpdateCategoryAndPeriodWhenNoConflict



} // BudgetIntegrationTest 
