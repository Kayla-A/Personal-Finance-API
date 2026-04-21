package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.Account;
import com.kaylaarthur.financeapi.enums.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;


@Repository
public class AccountRepo {

    private final DataSource dataSource;

    public AccountRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    } // AccountRepo

    public Account save(Account account) {
        String sql = "INSERT INTO Accounts (user_id, name, type, balance) VALUES (?, ?, ?, ?)";

       try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, account.getUserId());
            stmt.setString(2, account.getName());
            stmt.setString(3, account.getType().name());
            stmt.setBigDecimal(4, account.getBalance());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    account.setAccountId(rs.getLong(1));
                } else {
                    throw new RuntimeException("Failed to retrieve genrated account Id");
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error saving account", e);
        } // try-catch

        return account;
    } // saveAccount

    public Optional<Account> findByUserIdAndName(long userId, String name) {
        String sql = "SELECT * FROM Accounts Where user_id = ? AND name = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, name);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowToAccount(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding account by id and name", e);
        } // try-catch

        return Optional.empty();
    } // findByName

    private Account mapRowToAccount(ResultSet rs) throws SQLException {
        return new Account(
                        rs.getLong("account_id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        Type.valueOf(rs.getString("type")),
                        rs.getBigDecimal("balance")
                    );
    } // mapRowToUser
    
} // AccountRepo
