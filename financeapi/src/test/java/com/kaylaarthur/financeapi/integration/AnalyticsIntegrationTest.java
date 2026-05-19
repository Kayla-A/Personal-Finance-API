package com.kaylaarthur.financeapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

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

    private long makeTransaction(
        long categoryId, 
        long accountId, 
        double amount,
        LocalDate date,
        String type) throws Exception {
        
        String response = mockMvc.perform(post("/transactions")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            { 
                "categoryId": %d,
                "accountId": %d ,
                "amount": %f,
                "date": "%s",
                "description": "TESTING TESTING",
                "transactionType": "%s"
            }
        """.formatted(categoryId, accountId, amount, date.toString(), type)))
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
    } // shouldCalculateBudgetUsage

    // will fail since H@ is stricter than MySQL, will test in an actual MySQL database instead 
    @Test 
    void shouldGetMonthlySummary() throws Exception {
        long checkingId = makeAccount("Checking", "CHECKINGS", 2000);
    
        long foodId = makeCategory("Food");
        long salaryId = makeCategory("Salary");

        // January
        makeTransaction(foodId, checkingId, 100, "EXPENSE");
        makeTransaction(foodId, checkingId, 50, "EXPENSE");
        makeTransaction(salaryId, checkingId, 1000, "INCOME");

        // February
        mockMvc.perform(post("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": "%d",
                    "accountId": "%d",
                    "amount": 200.00,
                    "date": "2026-02-10",
                    "description": "Groceries",
                    "transactionType": "EXPENSE"
                }
            """.formatted(foodId, checkingId)))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": "%d",
                    "accountId": "%d",
                    "amount": 1500.00,
                    "date": "2026-02-15",
                    "description": "Paycheck",
                    "transactionType": "INCOME"
                }
            """.formatted(salaryId, checkingId)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/analytics/monthly-summary")
            .header("Authorization", token)
            .param("startDate", "2026-01")
            .param("endDate", "2026-02"))
            .andExpect(status().isOk())

            // two months returned
            .andExpect(jsonPath("$.length()").value(2))

            // January checks
            .andExpect(jsonPath("$[0].totalExpense").value(150.00))
            .andExpect(jsonPath("$[0].totalIncome").value(1000.00))

            // February checks
            .andExpect(jsonPath("$[1].totalExpense").value(200.00))
            .andExpect(jsonPath("$[1].totalIncome").value(1500.00));
    } // shouldGetMonthlySummary

    @Test
    void shouldGetSpendingByCategory() throws Exception {
        long checkingId = makeAccount("Checking", "CHECKINGS", 2000);

        long foodId = makeCategory("Food");
        long rentId = makeCategory("Rent");

        // Food total = 150
        makeTransaction(foodId, checkingId, 100, "EXPENSE");
        makeTransaction(foodId, checkingId, 50, "EXPENSE");

        // Rent total = 1000
        makeTransaction(rentId, checkingId, 1000, "EXPENSE");

        // Income should NOT count toward spending
        makeTransaction(foodId, checkingId, 5000, "INCOME");

        mockMvc.perform(get("/analytics/category-spending")
            .header("Authorization", token)
            .param("startDate", "2026-01-01")
            .param("endDate", "2026-12-31"))
            .andExpect(status().isOk())

            // two categories returned
            .andExpect(jsonPath("$.length()").value(2))

            // Food 
            .andExpect(jsonPath("$[0].categoryName").value("Food"))
            .andExpect(jsonPath("$[0].totalSpent").value(150.00))
            .andExpect(jsonPath("$[0].averageTransactionSize").value(75.00))
            // Rent
            .andExpect(jsonPath("$[1].categoryName").value("Rent"))
            .andExpect(jsonPath("$[1].totalSpent").value(1000.00))
            .andExpect(jsonPath("$[1].averageTransactionSize").value(1000.00));
    } // shouldGetSpendingByCategory

    @Test
    void shouldGetBudgetOverruns() throws Exception {
        long accountId = makeAccount("Checking", "CHECKINGS", 5000);

        long foodId = makeCategory("Food");
        long rentId = makeCategory("Rent");

        // Food budget = 200
        makeBudget(foodId, 200, "MONTHLY");

        // Rent budget = 1500
        makeBudget(rentId, 1500, "MONTHLY");

        // Food spending = 300 (OVER budget)
        makeTransaction(foodId, accountId, 100, LocalDate.now(), "EXPENSE");
        makeTransaction(foodId, accountId, 200, LocalDate.now(), "EXPENSE");

        // Rent spending = 1000 (UNDER budget)
        makeTransaction(rentId, accountId, 1000, LocalDate.now(), "EXPENSE");

        mockMvc.perform(get("/analytics/budget-overrun")
            .header("Authorization", token))
            .andExpect(status().isOk())

            // only Food should appear
            .andExpect(jsonPath("$.length()").value(1))

            .andExpect(jsonPath("$[0].categoryName").value("Food"))
            .andExpect(jsonPath("$[0].budgetLimit").value(200.00))
            .andExpect(jsonPath("$[0].actualSpent").value(300.00))

            .andExpect(jsonPath("$[0].percentOverBudget").value(50.00))

            .andExpect(jsonPath("$[0].period").value("MONTHLY"));
    } // shouldGetBudgetOverruns

} // AnalyticsIntegrationTest
