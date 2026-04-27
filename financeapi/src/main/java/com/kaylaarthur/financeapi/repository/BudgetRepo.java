package com.kaylaarthur.financeapi.repository;

import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.Budget;

import java.sql.Statement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

@Repository
public class BudgetRepo {

    private final DataSource dataSource;
    
    public BudgetRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    } // BudgetRepo


    public Budget save(Budget budget) {
        String sql = "INSERT INTO Budgets (user_id, category_id, budget_limit, period) VALUES(?, ?, ?, ?)";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, budget.getUserId());
            stmt.setLong(2, budget.getCategoryId());
            stmt.setBigDecimal(3, budget.getBudgetLimit());
            stmt.setString(4, budget.getPeriod().name());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    budget.setBudgetId(rs.getLong(1));
                } else {
                    throw new RuntimeException("Failed to retrieve generated budget id");
                } // if
            } // try
        } catch(SQLException e) {
            throw new RuntimeException("Error saving budget", e);
        } // try

        return budget;
    } // save 

    public Budget update(Budget budget) {
        String sql = "UPDATE Budgets SET category_id = ?, budget_limit = ?, period = ?";

         try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, budget.getCategoryId());
            stmt.setBigDecimal(2, budget.getBudgetLimit());
            stmt.setString(3, budget.getPeriod().name());
            
            stmt.executeUpdate();

        } catch(SQLException e) {
            throw new RuntimeException("Error updating budget", e);
        } // try-catch

        return budget;
    } // update

    public void delete(long budgetId) {
         String sql = "DELETE FROM Budgets WHERE budget_id = ?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, budgetId);

            int row = stmt.executeUpdate();

            if(row == 0) {
                throw new RuntimeException("Error finding or deleting budget");
            } // if

        } catch(SQLException e) {
            throw new RuntimeException("Error deleting budget", e);
        } // try-catch
    } // delete


    public Optional<Budget> findByUserIdAndCategoeyIdAndPeriod( long userId, long categoryId, BudgetInterval period) {
        String sql = """
            SELECT * 
            FROM Budgets
            WHERE user_id = ?
                AND category_id = ?
                AND period = ?
        """;
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, categoryId);
            stmt.setString(3, period.name());

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowToBudget(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding budget by user id, category id and budget limit", e);
        } // try-catch

        return Optional.empty();
    } // findByUserIdAndCategoeyIdAndPeriod

    public Optional<Budget> findByUserIdAndBudgetId(long userId, long budgetId) {
        String sql = "SELECT * FROM Budgets WHERE user_id = ? AND budget_id = ?";

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, budgetId);


            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapRowToBudget(rs));
                } // if
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding budget by user id and budget id", e);
        } // try-catch

        return Optional.empty();
    } // findByUserIdAndBudgetId

    public List<Budget> findAllBudgets(long userId, Long categoryId, BigDecimal minLimit, BigDecimal maxLimit, BudgetInterval period) {
        StringBuilder sql = new StringBuilder("""
            SELECT * 
            FROM Budgets
            WHERE user_id = ?        
        """);

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if(categoryId != null) {
            sql.append("AND categroy_id = ?");
            params.add(categoryId);
        } // if

        if(minLimit != null) {
            sql.append("AND budget_limit >= ?");
            params.add(minLimit);
        } // if

        if(maxLimit != null) {
            sql.append("AND budget_limit =< ?");
            params.add(maxLimit);
        } // if

        if(period != null) {
            sql.append("AND period = ?");
            params.add(period);
        } // if

        ArrayList<Budget> budgets = new ArrayList<>();
        
        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for(int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            } // for

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    budgets.add(mapRowToBudget(rs));
                } // if
                
            } // try

        } catch(SQLException e) {
            throw new RuntimeException("Error finding transactions", e);
        } // try-catch
        return budgets;
    } // findAllBudgets


    private Budget mapRowToBudget(ResultSet rs) throws SQLException {
        return new Budget(
            rs.getLong("budget_id"),
            rs.getLong("user_id"),
            rs.getLong("category_id"),
            rs.getBigDecimal("budget_limit"),
            BudgetInterval.valueOf(rs.getString("period"))
        );
    } // mapRowToBudget
} // BudgetRepo
