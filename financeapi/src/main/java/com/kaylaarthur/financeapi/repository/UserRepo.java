package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.User;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class UserRepo {
    
    private final DataSource dataSource;
    

    public UserRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    } // UserRepo

    /**
     * 
     * @param user
     * @return
     */
    public User save(User user) {
        String sql = "INSERT INTO Users (name, email, hashed_password) VALUES (?, ?, ?)";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getHashedPassword());

            stmt.executeUpdate();
            
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    user.setId(rs.getLong(1));
                } else {
                    throw new RuntimeException("Failed to retrieve generated Id");
                } // if
            } // try
             
        } catch(SQLException e) {
            throw new RuntimeException("Error saving user", e);
        } // try-catch

        return user;
    } // save

    /**
     * 
     * @param name
     * @return
     */
    public Optional<User> findByName(String name) {
        String sql = "SELECT * FROM Users Where name = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowTUser(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding user by name", e);
        } // try-catch

        return Optional.empty();
    } // finsByName

    /**
     * 
     * @param email
     * @return
     */
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM Users Where email = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowTUser(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding user by email", e);
        } // try-catch

        return Optional.empty();
    } // finsByEmail

    /**
     * Maps the rows of the Result Set to the User enitty model.
     * @param rs The result set containing the rows to map.
     * @return The new User entity.
     * @throws SQLException
     */
    private User mapRowTUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("hashed_password")
                    );
    } // mapRowToUser

} // userRepo
