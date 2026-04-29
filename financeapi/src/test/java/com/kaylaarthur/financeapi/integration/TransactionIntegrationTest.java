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
public class TransactionIntegrationTest {
    
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

    @Test
    void shouldCreateExpenseTransaction() throws Exception{
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        mockMvc.perform(post("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d,
                    "accountId": %d,
                    "amount": 100.00,
                    "date": "2026-01-01",
                    "description": "Lunch",
                    "transactionType": "EXPENSE"
                }
            """.formatted(categoryId, accountId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.amount").value(100.00));
    } // shouldCreateExpenseTransaction

    @Test
    void shouldCreateIncomeTransaction() throws Exception {
        long accountId = makeAccount("TestAccount", "SAVINGS", 1000);
        long categoryId = makeCategory("Salary");

        mockMvc.perform(post("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d,
                    "accountId": %d,
                    "amount": 100.00,
                    "date": "2026-01-01",
                    "description": "Got paid!",
                    "transactionType": "INCOME"
                }
            """.formatted(categoryId, accountId)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.amount").value(100.00));
    } // shouldCreateIncomeTransaction

    @Test
    void shouldRejectInvalidAccount() throws Exception {
        long accountId = 99;
        long categoryId = makeCategory("Salary");

        mockMvc.perform(post("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d,
                    "accountId": %d,
                    "amount": 100.00,
                    "date": "2026-01-01",
                    "description": "Got paid!",
                    "transactionType": "INCOME"
                }
            """.formatted(categoryId, accountId)))
            .andExpect(status().isBadRequest());
    } // shouldRejectInvalidAccount

    @Test
    void shouldRejectInvalidCategory() throws Exception {
        long accountId = makeAccount("TestAccount", "SAVINGS", 1000);
        long categoryId = 99;

        mockMvc.perform(post("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d,
                    "accountId": %d,
                    "amount": 100.00,
                    "date": "2026-01-01",
                    "description": "Got paid!",
                    "transactionType": "INCOME"
                }
            """.formatted(categoryId, accountId)))
            .andExpect(status().isBadRequest());
    } // shouldRejectInvalidCategory

    @Test
    void shouldRejectExpenseThatCausesNegativebalance() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 50);
        long categoryId = makeCategory("Food");

        mockMvc.perform(post("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d,
                    "accountId": %d,
                    "amount": 100.00,
                    "date": "2026-01-01",
                    "description": "NEGATIVE",
                    "transactionType": "EXPENSE"
                }
            """.formatted(categoryId, accountId)))
            .andExpect(status().isBadRequest());
    } // shouldRejectExpenseThatCausesNegativebalance

    void shouldRejectUpdateThatCausesNegativebalance() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 50);
        long categoryId = makeCategory("Food");

        makeTransaction(categoryId, accountId, 25, "EXPENSE");
        
        mockMvc.perform(put("/transactions")
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "categoryId": %d,
                    "accountId": %d,
                    "amount": 100.00,
                    "date": "2026-01-01",
                    "description": "NEGATIVE",
                    "transactionType": "EXPENSE"
                }
            """.formatted(categoryId, accountId)))
            .andExpect(status().isBadRequest());
    } // 

    @Test
    void shouldDecreseBalanceForExpense() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        makeTransaction(categoryId, accountId, 100, "EXPENSE");

        mockMvc.perform(get("/accounts/" + accountId)
            .header("Authorization", token))
            .andExpect(jsonPath("$.balance").value(900.00));
    } // shouldDecreseBalanceForExpense

    @Test
    void shouldIncreseBalanceForIncome() throws Exception{
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Salary");

        makeTransaction(categoryId, accountId, 500, "INCOME");
        
        mockMvc.perform(get("/accounts/" + accountId)
            .header("Authorization", token))
            .andExpect(jsonPath("$.balance").value(1500.00));
    } // shouldIncreseBalanceForIncome

    @Test
    void shouldGetTransactionById() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        long tId = makeTransaction(categoryId, accountId, 100, "EXPENSE");
        
        mockMvc.perform(get("/transactions/" + tId)
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.transactionId").value(tId));

    } // shouldGetTransactionById

    @Test
    void shouldGetAllTransactionsByAccount() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long accountId2 = makeAccount("TestAccount2", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        makeTransaction(categoryId, accountId, 100, "EXPENSE");
        makeTransaction(categoryId, accountId2, 100, "EXPENSE");

        mockMvc.perform(get("/transactions")
            .param("accountId", String.valueOf(accountId))
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));

    } // shouldGetAllTransactions

    @Test
    void shouldGetAllTransactionsByCategory() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");
        long categoryId2 = makeCategory("Travel");

        makeTransaction(categoryId, accountId, 100, "EXPENSE");
        makeTransaction(categoryId2, accountId, 500, "EXPENSE");

        mockMvc.perform(get("/transactions")
            .param("categoryId", String.valueOf(categoryId))
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));

    } // shouldGetAllTransactionsByCategory

    @Test
    void shouldGetAllTransactionsByDateRange() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        makeTransaction(categoryId, accountId, 100, "EXPENSE");

        mockMvc.perform(get("/transactions")
            .param("startDate", "2025-12-12")
            .param("endDate", "2026-04-29")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));

    } // shouldGetAllTransactionsByDateRange
    
    @Test
    void shouldUpdateTransaction() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        long tId = makeTransaction(categoryId, accountId, 100, "EXPENSE");
        
        mockMvc.perform(put("/transactions/" + tId)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "amount": 200.00,
                    "description": "Updated"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(200.00))
            .andExpect(jsonPath("$.description").value("Updated"));

    } // shouldUpdateTransaction

    @Test
    void shouldAdjustBalanceWhenUpdatingTransaction() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        long tId = makeTransaction(categoryId, accountId, 100, "EXPENSE");
        
        mockMvc.perform(put("/transactions/" + tId)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "amount": 200.00 }
            """))
            .andExpectAll(status().isOk());

        mockMvc.perform(get("/accounts/" + accountId)
            .header("Authorization", token))
            .andExpect(jsonPath("$.balance").value(800.00));
    } // shouldAdjustBalanceWhenUpdatingTransaction

    @Test
    void shouldAdjustBalanceWhenChangingType() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        long tId = makeTransaction(categoryId, accountId, 100, "EXPENSE");
        
        mockMvc.perform(put("/transactions/" + tId)
            .header("Authorization", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "transactionType": "INCOME" }
            """))
            .andExpectAll(status().isOk());

        mockMvc.perform(get("/accounts/" + accountId)
            .header("Authorization", token))
            .andExpect(jsonPath("$.balance").value(1100.00));

    } // shouldAdjustBalanceWhenChangingType


    @Test
    void shouldDeleteTransaction() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        long tId = makeTransaction(categoryId, accountId, 100, "EXPENSE");
        
        mockMvc.perform(delete("/transactions/" + tId)
            .header("Authorization", token))
            .andExpect(status().isNoContent());
    } // shouldDeleteTransaction

    @Test
    void shouldRestoreBalanceOnDelete() throws Exception {
        long accountId = makeAccount("TestAccount", "CHECKINGS", 1000);
        long categoryId = makeCategory("Food");

        long tId = makeTransaction(categoryId, accountId, 100, "EXPENSE");
        
        mockMvc.perform(delete("/transactions/" + tId)
            .header("Authorization", token))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/" + accountId)
            .header("Authorization", token))
            .andExpect(jsonPath("$.balance").value(1000.00));
 
    } // shouldRestoreBalanceOnDelete

    @Test
    void shouldRejectWithoutToken() throws Exception {
        mockMvc.perform(delete("/transactions"))
            .andExpectAll(status().isUnauthorized());
    } // shouldRejectWithoutToken

} // TransactionIntegrationTest
