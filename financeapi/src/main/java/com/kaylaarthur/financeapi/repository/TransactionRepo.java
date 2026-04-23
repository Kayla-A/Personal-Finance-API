package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.Transaction;
import com.kaylaarthur.financeapi.enums.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
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
        String sql = "UPDATE Transactions SET amount = ?, date = ?, description = ?, transaction_type = ? WHERE tranaction_id = ? AND account_id = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, transaction.getAmount());
            stmt.setDate(2, Date.valueOf(transaction.getDate()));
            stmt.setString(3, transaction.getDescription());
            stmt.setString(5, transaction.getTransactionType().name());
            stmt.setLong(6, transaction.getTransactionId());
            stmt.setLong(6, transaction.getAccountId());

            stmt.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        } // try-catch

        return transaction;
    } // update

    public void delete(long userId, long transactionId) {
        String sql = "DELETE FROM Transactions WHERE user_id = ? and transaction_id = ?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, transactionId);

            stmt.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        } // try-catch
    } // delete

    public Optional<Transaction> findByUserIdAndTransactionId(long userId, long transactionId) {
        String sql = "SELECT * FROM Transaction Where user_id = ? AND transaction_id = ?";
        
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
            throw new RuntimeException("Error finding account by userId and accountId", e);
        } // try-catch

        return Optional.empty();
    } // findByUserIdAndTransactionId

    public List<Transaction> findTransactionsByAccountId(long userId, long accountId) {
        String sql = """
                SELECT * 
                FROM Transactions t, Accounts a
                WHERE a.account_id = ?
                    AND a.user_id = ?
                    AND t.account_id = a.account_id
        """;
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountId);
            stmt.setLong(2, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                } // if
                
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding transactions by accountId", e);
        } // try-catch

        return transactions;
    } // findTransactionsByAccountId

    public List<Transaction> findTransactionsByUserId(long userId) {
        String sql = "SELECT * FROM Transactions Where user_id = ?";
        ArrayList<Transaction> transactions = new ArrayList<>();

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
                        rs.getBigDecimal("ammount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("description"),
                        TransactionType.valueOf(rs.getString("transaction_type"))
                    );
    } // mapRowToTransaction
    
} // TransactionRepo
