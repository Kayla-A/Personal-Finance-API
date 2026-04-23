package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.Account;
import com.kaylaarthur.financeapi.model.Transaction;
import com.kaylaarthur.financeapi.enums.Type;
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
