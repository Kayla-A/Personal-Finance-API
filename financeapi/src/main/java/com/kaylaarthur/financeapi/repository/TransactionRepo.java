package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.Transaction;
import com.kaylaarthur.financeapi.response.BudgetOverrunResponse;
import com.kaylaarthur.financeapi.response.CategorySpendingResponse;
import com.kaylaarthur.financeapi.response.MonthlySummaryResponse;
import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.enums.TransactionType;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepo {
    
    private final DataSource dataSource;

    public TransactionRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    } // TransactionRepo

    public Transaction save(Transaction transaction) {
        String sql = "INSERT INTO Transactions (category_id, account_id, amount, date, description, transaction_type) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, transaction.getCategoryId());
                stmt.setLong(2, transaction.getAccountId());
                stmt.setBigDecimal(3, transaction.getAmount());
                stmt.setDate(4, Date.valueOf(transaction.getDate()));
                stmt.setString(5, transaction.getDescription());
                stmt.setString(6, transaction.getTransactionType().name());

                stmt.executeUpdate();

                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if(rs.next()) {
                        transaction.setTransactionId(rs.getLong(1));
                    } else {
                        throw new RuntimeException("Failed to retrieve generated transaction id");
                    } // if
                } // try
            } catch(SQLException e) {
                throw new RuntimeException("Error saving transaction", e);
            } // try

            return transaction;
    } // save


    public Transaction update(Transaction transaction) {
        String sql = "UPDATE Transactions SET amount = ?, date = ?, description = ?, transaction_type = ? WHERE transaction_id = ? AND account_id = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, transaction.getAmount());
            stmt.setDate(2, Date.valueOf(transaction.getDate()));
            stmt.setString(3, transaction.getDescription());
            stmt.setString(4, transaction.getTransactionType().name());
            stmt.setLong(5, transaction.getTransactionId());
            stmt.setLong(6, transaction.getAccountId());

            stmt.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        } // try-catch

        return transaction;
    } // update

    public void delete(long transactionId) {
        String sql = "DELETE FROM Transactions WHERE transaction_id = ?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, transactionId);

            int row = stmt.executeUpdate();

            if(row != 1) {
                throw new RuntimeException("Error deleting transaction");
            } // if

        } catch(SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        } // try-catch
    } // delete

    public Optional<Transaction> findByUserIdAndTransactionId(long userId, long transactionId) {
        String sql = """
            SELECT t.* 
            FROM Transactions t 
            JOIN Accounts a 
            ON t.account_id = a.account_id
            Where a.user_id = ? 
                AND t.transaction_id = ?
        """;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, transactionId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowToTransaction(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding transaction by userId and transactionId", e);
        } // try-catch

        return Optional.empty();
    } // findByUserIdAndTransactionId

    public List<Transaction> findAllTransactions(long userId, Long accountId, Long categoryId, TransactionType type, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder("""
                SELECT * 
                FROM Transactions t
                JOIN Accounts a
                ON t.account_id = a.account_id
                WHERE a.user_id = ?
        """);
        
        List<Object> params = new ArrayList<>();
        params.add(userId);
        
        if(accountId != null) {
            sql.append(" AND t.account_id = ?");
            params.add(accountId);
        } // if

        if(categoryId != null) {
            sql.append(" AND t.category_id = ?");
            params.add(categoryId);
        } // if

        if(type != null) {
            sql.append(" AND t.transaction_type = ?");
            params.add(type.name());
        } // if

        if(startDate != null) {
            sql.append(" AND t.date >= ?");
            params.add(Date.valueOf(startDate));
        } // if

        if(endDate != null) {
            sql.append(" AND t.date<= ?");
            params.add(Date.valueOf(endDate));
        } // if

        sql.append(" ORDER BY t.date DESC");

        List<Transaction> transactions = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for(int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            } // for

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                } // if
                
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding transactions", e);
        } // try-catch

        return transactions;
    } // findTransactionsByAccountId

    public List<Transaction> findTransactionsByUserId(long userId) {
        String sql = "SELECT * FROM Transactions Where user_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                } // if
                
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding transactions by userId", e);
        } // try-catch

        return transactions;
    } // findTransactionsByUserId


    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                        rs.getLong("transaction_id"),
                        rs.getLong("category_id"),
                        rs.getLong("account_id"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("description"),
                        TransactionType.valueOf(rs.getString("transaction_type"))
                    );
    } // mapRowToTransaction
    

    public BigDecimal sumExpensesByCategoryAndPeriod(long userId, long categoryId, BudgetInterval period) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transactions t
            JOIN Accounts a
            ON t.account_id = a.account_id
            WHERE a.user_id = ?
                AND t.category_id = ?
                AND t.transaction_type = 'EXPENSE'
        """;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, userId);
            stmt.setLong(2, categoryId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) { 
                    BigDecimal result = rs.getBigDecimal(1); 
                    return result != null ? result : BigDecimal.ZERO;
                }
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error calculating spending by category and period", e);
        } // try

        return BigDecimal.ZERO;
    } // sumExpensesByCategoryAndPeriod

    public List<MonthlySummaryResponse> monthlySummary(long userId, YearMonth startDate, YearMonth endDate) {
        String sql = """
            SELECT 
                YEAR(t.date) as trans_year,
                MONTH(t.date) as trans_month,
                sum(
                    CASE
                        WHEN t.transaction_type = 'EXPENSE'
                        THEN t.amount
                        ELSE 0
                    END
                ) as total_expense,
                sum(
                    CASE
                        WHEN t.transaction_type = 'INCOME'
                        THEN t.amount
                        ELSE 0
                    END
                ) as total_income,
                (
                    SELECT c.category_name
                    FROM Transactions t2
                    JOIN Categories c
                        ON t2.category_id = c.category_id
                    JOIN Accounts a2
                        ON t2.account_id = a2.account_id
                    WHERE a2.user_id = a.user_id
                        AND YEAR(t2.date) = YEAR(t.date)
                        AND MONTH(t2.date) = MONTH(t.date)
                    GROUP BY c.category_id, c.category_name
                    ORDER BY count(*) desc LIMIT 1
                ) as most_frequent_category
            From Transactions t
            JOIN Accounts a
                ON a.account_id = t.account_id
            WHERE a.user_id = ?
                AND t.date >= ?
                AND t.date <= ?
            GROUP BY YEAR(t.date), MONTH(t.date)
            ORDER BY  YEAR(t.date), MONTH(t.date) desc
        """;
        List<MonthlySummaryResponse> summary = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setDate(2, Date.valueOf(startDate.atDay(1)));
            stmt.setDate(3, Date.valueOf(endDate.atEndOfMonth()));

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    summary.add(new MonthlySummaryResponse(
                        rs.getInt("trans_year"), 
                        rs.getInt("trans_month"),
                        rs.getBigDecimal("total_expense"),
                        rs.getBigDecimal("total_income"),
                        rs.getString("most_frequent_category")));
                } // if
                
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error getting monthly summary", e);
        } // try-catch

        return summary;
    } // monthlySummary

    public List<CategorySpendingResponse> spendingByCategory(
        long userId, 
        LocalDate startDate, 
        LocalDate endDate,
        Long accountId,
        BigDecimal minAmount
    ) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                c.category_name as category_name,
                SUM(t.amount) as total_spent,
                ROUND(
                    SUM(t.amount) / 
                    (   
                        SELECT SUM(t.amount) 
                        FROM Transactions t, Categories c 
                        WHERE c.user_id = ? 
        """);
        
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if(accountId != null) {
            sql.append(" AND t.account_id = ?");
            params.add(accountId);
        } // if

        if(minAmount != null) {
            sql.append(" AND t.amount >= ?");
            params.add(minAmount);
        } // if

        sql.append("""
                            AND c.category_id = t.category_id
                            AND t.date BETWEEN ? AND ?
                            AND t.transaction_type = 'EXPENSE'
                    ) * 100, 2) as percent_of_spendings,
                ROUND(SUM(t.amount)/COUNT(t.transaction_id), 2) as average_trans_size
            FROM Transactions t
            JOIN Categories c
            ON t.category_id = c.category_id
            WHERE c.user_id = ?
                AND t.date BETWEEN ? AND ? 
                AND t.transaction_type = 'EXPENSE' 
        """);
        
        params.add(startDate);
        params.add(endDate);
        params.add(userId);
        params.add(startDate);
        params.add(endDate);

        if(accountId != null) {
            sql.append(" AND t.account_id = ?");
            params.add(accountId);
        } // if

        if(minAmount != null) {
            sql.append(" AND t.amount >= ?");
            params.add(minAmount);
        } // if

        sql.append("""
             GROUP BY c.category_name
            ORDER BY total_spent
        """);

        List<CategorySpendingResponse> responses = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for(int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            } // for

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    responses.add(new CategorySpendingResponse(
                        rs.getString("category_name"), 
                        rs.getBigDecimal("total_spent"),
                        rs.getDouble("percent_of_spendings"),
                        rs.getBigDecimal("average_trans_size")));
                } // while
                
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error getting spending by category", e);
        } // try-catch

        return responses;
    } // spendingByCategory

    public List<BudgetOverrunResponse> budgetOverrun(long userId) {
        List<BudgetOverrunResponse> responses = new ArrayList<>();

        String sql = """
            SELECT 
                c.category_name as category_name,
                b.budget_limit as budget_limit,
                sum(t.amount) as actual_spent,
                b.period as period
            FROM Transactions t
            JOIN Categories c
            ON t.category_id = c.category_id
            JOIN Budgets b
            ON c.category_id = b.category_id
            WHERE c.user_id = ?
                AND b.user_id = ?
                AND t.date <= ?
                AND t.transaction_type = 'EXPENSE'
                AND t.date >= CASE  
                    WHEN b.period = 'MONTHLY' THEN ?
                    WHEN b.period = 'YEARLY' THEN ?
                END
            GROUP BY b.budget_id, c.category_name, b.budget_limit, b.period
            HAVING sum(t.amount) > b.budget_limit
            ORDER BY actual_spent desc
        """;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setDate(4, Date.valueOf(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())));
            stmt.setDate(5, Date.valueOf(LocalDate.now().with(TemporalAdjusters.firstDayOfYear())));

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    responses.add(new BudgetOverrunResponse(
                        rs.getString("category_name"), 
                        rs.getBigDecimal("budget_limit"),
                        rs.getBigDecimal("actual_spent"),
                        BudgetInterval.valueOf(rs.getString("period"))
                    ));
                } // while
                
            } // try

        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting budget overrun by category", e);
        } // try-catch

        return responses;
    } // budgetOverrun

    public BigDecimal totalSpent(
            long userId, 
            Long accountId, 
            Long categoryId, 
            LocalDate startDate, 
            LocalDate endDate
        ) {
            StringBuilder sql = new StringBuilder("""
                SELECT 
                    COALESCE(sum(t.amount), 0) as total_spent
                FROM Transactions t
                JOIN Accounts a
                    ON t.account_id = a.account_id
                JOIN Categories c
                    ON t.category_id = c.category_id
                WHERE t.date BETWEEN ? AND ?
                    AND a.user_id = ?
                    AND t.transaction_type = 'EXPENSE'
            """);
            
            List<Object> params = new ArrayList<>();
            params.add(startDate);
            params.add(endDate);
            params.add(userId);

            if(accountId != null) {
                sql.append(" AND t.account_id = ?");
                params.add(accountId);
            } // if

            if(categoryId != null) {
                sql.append(" AND t.category_id = ?");
                params.add(categoryId);
            } // if

            try(Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                
                for(int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                } // for

                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()) {
                        return rs.getBigDecimal("total_spent");
                    } // if
                } // try

            } catch(SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error getting total spent", e);
            } // try-catch

            return BigDecimal.ZERO;
        } // burnRate

} // TransactionRepo
