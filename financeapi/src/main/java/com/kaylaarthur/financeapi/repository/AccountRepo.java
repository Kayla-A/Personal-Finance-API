package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;


@Repository
public class AccountRepo {

    private final DataSource dataSource;

    public AccountRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    } // AccountRepo

    public Account saveAccount(Account account) {
        String sql = "INSERT INTO Accounts (user_id, type, balance) VALUES (?, ?, ?)";

       try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, account.getUserId());
            stmt.setString(2, account.getType().name());
            stmt.setBigDecimal(3, account.getBalance());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    account.setAccountId(1);
                } else {
                    throw new RuntimeException("Failed to retrieve genrated account Id");
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error saving account", e);
        } // try-catch

        return account;
    } // saveAccount
    
} // AccountRepo
