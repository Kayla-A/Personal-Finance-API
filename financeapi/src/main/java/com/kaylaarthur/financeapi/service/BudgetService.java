package com.kaylaarthur.financeapi.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.Budget;
import com.kaylaarthur.financeapi.repository.BudgetRepo;
import com.kaylaarthur.financeapi.request.AddBudgetRequest;
import com.kaylaarthur.financeapi.request.UpdateBudgetRequest;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class BudgetService {
    
    private BudgetRepo budgetRepo;

    public BudgetService(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    } // BudgetService

    public Budget addBudget(long userId, AddBudgetRequest request) {
        Budget budget = new Budget(
            userId,
            request.getCategoryId(),
            request.getBudgetLimit(),
            request.getPeriod()
        );

        // check budget doesn't already exist
        budgetRepo.findByUserIdAndCategoryIdAndPeriod(
            budget.getUserId(), 
            budget.getCategoryId(), 
            budget.getPeriod()
        ).ifPresent(u -> { throw new IllegalArgumentException("Budget not unique by category and period"); });
        
        return budgetRepo.save(budget);
    } // addBudget

    @Transactional
    public Budget updateBudget(long userId, long budgetId, UpdateBudgetRequest request) {
        // check for & get budget
        Budget budget = budgetRepo.findByUserIdAndBudgetId(userId, budgetId)
            .orElseThrow( () -> new RuntimeException("Budget not found"));
        
        Long newCategoryId = request.getCategoryId() != null ? request.getCategoryId() : budget.getCategoryId();
        BudgetInterval newPeriod = request.getPeriod() != null ? request.getPeriod() : budget.getPeriod();

        if(request.getCategoryId() != null || request.getPeriod() != null) {
            budgetRepo.findByUserIdAndCategoryIdAndPeriod(userId, newCategoryId, newPeriod)
                .ifPresent(existingBudget -> {
                    if(existingBudget.getBudgetId() != budgetId) {
                        throw new IllegalArgumentException("Budget not unique by category and period");
                    } // if
                });
        } // if
        
        if(request.getCategoryId() != null) { budget.setCategoryId(request.getCategoryId()); } // if
        if(request.getBudgetLimit() != null) { budget.setBudgetLimit(request.getBudgetLimit()); } // if
        if(request.getPeriod() != null) { budget.setPeriod(request.getPeriod()); } // if

        return budgetRepo.update(budget);
    } // updateBudget

    public void deleteBudget(long userId, long budgetId) {
        budgetRepo.findByUserIdAndBudgetId(userId, budgetId).orElseThrow(() -> new RuntimeException("Budget not found"));
        budgetRepo.delete(budgetId);
    } // deleteBudget

    public Budget getBudget(long userId, long budgetId) {
        return budgetRepo.findByUserIdAndBudgetId(userId, budgetId).orElseThrow(() -> new RuntimeException("Budget not found"));
    } // getBudget

    public List<Budget> getAllBudgets(long userId, Long categoryId, BigDecimal minLimit, BigDecimal maxLimit, BudgetInterval period) {
        return budgetRepo.findAllBudgets(userId, categoryId, minLimit, maxLimit, period);
    } // getAllBudgets
} // BudgetService
