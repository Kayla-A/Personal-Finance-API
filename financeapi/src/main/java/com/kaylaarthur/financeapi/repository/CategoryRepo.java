package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepo {

    private final DataSource dataSource;

    public CategoryRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    } // CategoryRepo

    public Category save(Category category) {
        String sql = "INSERT INTO Categories (user_id, name) VALUES (?, ?)";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, category.getUserId());
            stmt.setString(2, category.getCategoryName());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    category.setCategoryId(rs.getLong(1));
                } else {
                    throw new RuntimeException("Failed to retrieve genrated category Id");
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error saving category", e);
        } // try-catch

        return category;
    } // save

    public Category update(Category category) {
        String sql = "UPDATE Categories SET name = ? WHERE category_id = ? AND userID = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setLong(2, category.getCategoryId());
            stmt.setLong(3, category.getUserId());

            stmt.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException("Error updating account", e);
        } // try-catch

        return category;
    } // update

    public void delete(long categoryId, long userId) {
         String sql = "DELETE FROM Categories WHERE category_id = ? and user_id = ?";

       try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, categoryId);
            stmt.setLong(2, userId);

            stmt.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException("Error saving account", e);
        } // try-catch

    } // delete

    public List<Category> findByUserId(long userId) {
        String sql = "SELECT * FROM Categories Where user_id = ?";
        List<Category> categories = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    categories.add(mapRowToCategory(rs));
                } // if
                
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding categories by userId", e);
        } // try-catch

        return categories;
    } // findByUserId

    public Optional<Category> findByCategoryIdAndUserId(long categoryId, long userId) {
        String sql = "SELECT * FROM Categories Where category_id = ? AND user_id = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, categoryId);
            stmt.setLong(2, userId);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowToCategory(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding category by accountId and userId", e);
        } // try-catch

        return Optional.empty();

    } // findCategoryByCategoryIdAndUserId

    public Optional<Category> findByUserIdAndName(long userId, String name) {
        String sql = "SELECT * FROM Categories Where user_id = ? AND category_name = ?";
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, name);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowToCategory(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding category by userId and name", e);
        } // try-catch

        return Optional.empty();
    } // findCategoryByUserIdAndName





    private Category mapRowToCategory(ResultSet rs) throws SQLException {
        return new Category(
                        rs.getLong("category_id"),
                        rs.getLong("user_id"),
                        rs.getString("name")
                    );
    } // mapRowToCategory

} // CategoryRepo
