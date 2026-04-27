package com.kaylaarthur.financeapi.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.Budget;
import com.kaylaarthur.financeapi.repository.BudgetRepo;
import com.kaylaarthur.financeapi.request.AddBudgetRequest;
import com.kaylaarthur.financeapi.request.UpdateBudgetResquest;

import java.util.List;

@Service
public class BudgetService {
    
    private BudgetRepo budgetRepo;

    public BudgetService(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    } // BudgetService

    public Budget addBudget(long userId, AddBudgetRequest request) {
        // check users match
        if(userId != request.getUserId()) {throw new RuntimeException("Requesting users do not match"); }
        
        Budget budget = new Budget(
            request.getUserId(),
            request.getCategoryId(),
            request.getBudgetLimit(),
            request.getPeriod()
        );

        // check budget doesn't already exist
        budgetRepo.findByUserIdAndCategoeyIdAndPeriod(
            budget.getUserId(), 
            budget.getCategoryId(), 
            budget.getPeriod()
        ).ifPresent(u -> {throw new IllegalArgumentException("Budget not unique by category and period"); });
        
        return budgetRepo.save(budget);
    } // addBudget

    public Budget updateBudget(long userId, long budgetId, UpdateBudgetResquest request) {
        // check for & get budget
        Budget budget = budgetRepo.findByUserIdAndBudgetId(userId, budgetId).orElseThrow(() -> new RuntimeException("Budget not found"));
        
        if(request.getCategoryId() != null) {
            budgetRepo.findByUserIdAndCategoeyIdAndPeriod(
                budget.getUserId(), 
                request.getCategoryId(), 
                budget.getPeriod()
            ).ifPresent(u -> {throw new IllegalArgumentException("Budget not unique by category and period"); });
            budget.setCategoryId(request.getCategoryId());
        } // if
        if(request.getBudgetLimit() != null) { budget.setBudgetLimit(request.getBudgetLimit()); } // if
        if(request.getPeriod() != null) {
            budgetRepo.findByUserIdAndCategoeyIdAndPeriod(
                budget.getUserId(), 
                budget.getCategoryId(), 
                request.getPeriod()
            ).ifPresent(u -> {throw new IllegalArgumentException("Budget not unique by category and period"); });
            budget.setPeriod(request.getPeriod());
        } // if

        return budgetRepo.update(budget);
    } // updateBudget

    public void deleteBudget(long userId, long budgetId) {
        budgetRepo.findByUserIdAndBudgetId(userId, budgetId).orElseThrow(() -> new RuntimeException("Budget not found"));
        budgetRepo.delete(budgetId);
    } // deleteBudget

    public Budget getBudget(long usertId, long budgetId) {
        return budgetRepo.findByUserIdAndBudgetId(usertId, budgetId).orElseThrow(() -> new RuntimeException("Budget not found"));
    } // getBudget

    public List<Budget> getAllBudgets(long userId, Long categoryId, BigDecimal minLimit, BigDecimal maxLimit, BudgetInterval period) {
        return budgetRepo.findAllBudgets(userId, categoryId, minLimit, maxLimit, period);
    } // getAllBudgets
} // BudgetService
