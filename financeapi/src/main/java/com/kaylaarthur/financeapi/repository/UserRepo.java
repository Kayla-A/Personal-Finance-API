package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.User;

import org.springframework.stereotype.Repository;

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
    } // getConnection 

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
            stmt.setString(3, user.getPassword());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                user.setId(rs.getLong(1));
            } // if
        } catch(SQLException e) {
            e.printStackTrace();
        } // try-catch

        return user;
    } // save

    /**
     * 
     * @param name
     * @return
     */
    public User findByName(String name) {
        User user = null;
        String sql = "SELECT * FROM Users Where name = ?";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = new User(
                    rs.getLong("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("hashed_password")
                );
            } // if
        } catch(SQLException e) {
            e.printStackTrace();
        } // try-catch

        return user;
    } // finsByName

    /**
     * 
     * @param email
     * @return
     */
    public User findByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM Users Where email = ?";
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = new User(
                    rs.getLong("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("hashed_password")
                );
            } // if
        } catch(SQLException e) {
            e.printStackTrace();
        } // try-catch

        return user;
    } // finsByEmail


    /**
     * 
     * @param hashedPass
     * @return
     */
    public User findByHashed(String hashedPass) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE hashed_password = ?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hashedPass);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                user = new User(
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("hashed_password")
                );
            } // if

        } catch(SQLException e) {
            e.printStackTrace();
        } // try-catch

        return user;
    } // findByHash

} // userRepo
