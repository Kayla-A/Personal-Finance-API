package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.Budget;
import com.kaylaarthur.financeapi.repository.BudgetRepo;
import com.kaylaarthur.financeapi.repository.TransactionRepo;
import com.kaylaarthur.financeapi.response.BudgetUsageResponse;
import com.kaylaarthur.financeapi.response.MonthlySummaryResponse;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.YearMonth;

import java.util.List;

@Service
public class AnalyticsService {

    private BudgetRepo budgetRepo;

    private TransactionRepo transactionRepo;

    public AnalyticsService(BudgetRepo budgetRepo, TransactionRepo transactionRepo) {
        this.budgetRepo = budgetRepo;
        this.transactionRepo = transactionRepo;
    } // AnalyticsService

    public BudgetUsageResponse getBudgetUsage(long userId, long categoryId, BudgetInterval period) {
        // get budget
        Budget budget = budgetRepo
            .findByUserIdAndCategoryIdAndPeriod(userId, categoryId, period)
            .orElseThrow(()-> new RuntimeException("Budget not found"));
        // get total spent
        BigDecimal spent = transactionRepo.sumExpensesByCategoryAndPeriod(
            userId, categoryId, period
        );
        // build repsonse 
        return new BudgetUsageResponse(
            categoryId, 
            budget.getBudgetLimit(), 
            spent);
    } // getBudgetUsage

    public List<MonthlySummaryResponse> getMonthlySummary(long userId, YearMonth startDate, YearMonth endDate) {
        // check valid date range 
        if(!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        } // if

        return transactionRepo.monthlySummary(userId, startDate, endDate);
    } // getMonthlySummary
    
} // AnalyticsService
