package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.Budget;
import com.kaylaarthur.financeapi.repository.AccountRepo;
import com.kaylaarthur.financeapi.repository.BudgetRepo;
import com.kaylaarthur.financeapi.repository.CategoryRepo;
import com.kaylaarthur.financeapi.repository.TransactionRepo;
import com.kaylaarthur.financeapi.response.BudgetOverrunResponse;
import com.kaylaarthur.financeapi.response.BudgetUsageResponse;
import com.kaylaarthur.financeapi.response.BurnRateResponse;
import com.kaylaarthur.financeapi.response.CategorySpendingResponse;
import com.kaylaarthur.financeapi.response.MonthlySummaryResponse;
import com.kaylaarthur.financeapi.response.RecurringTransactionResponse;
import com.kaylaarthur.financeapi.response.SpendingTrendResponse;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private BudgetRepo budgetRepo;

    private TransactionRepo transactionRepo;
    private AccountRepo accountRepo;
    private CategoryRepo categoryRepo;

    public AnalyticsService(BudgetRepo budgetRepo, TransactionRepo transactionRepo,
        AccountRepo accountRepo, CategoryRepo categoryRepo
    ) {
        this.budgetRepo = budgetRepo;
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
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

    public List<CategorySpendingResponse> getSpendingByCategory(
        long userId, 
        LocalDate startDate, 
        LocalDate endDate,
        Long accountId,
        BigDecimal minAmount
    ) {
        // check valid date range 
        if(!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        } // if

        // check valid minAmount if given 
        if(minAmount != null && minAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum amount cannot be negative");
        } // if

        return transactionRepo.spendingByCategory(
            userId, 
            startDate, 
            endDate,
            accountId,
            minAmount
        );
    } // getSpendingByCategory

    public List<BudgetOverrunResponse> getBudgetOverrun(long userId) {
        return transactionRepo.budgetOverrun(userId);
    } // getBudgetOverrun

    public BurnRateResponse getBurnRate(
        long userId, 
        Long accountId, 
        Long categoryId, 
        LocalDate startDate, 
        LocalDate endDate
    ) {
        if(accountId != null) {
            // check account belongs to user 
            accountRepo.findByUserIdAndAccountId(userId, accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        } // if

        if(categoryId != null) {
            // check category belongs to user
            categoryRepo.findByCategoryIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        } // if
        
        // check valid date range 
        if(!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        } // if

        BigDecimal totalSpent = transactionRepo.totalSpent(
            userId, 
            accountId, 
            categoryId, 
            startDate, 
            endDate
        );

        BigDecimal balance = accountRepo.getTotalAccountBalance(userId, accountId);
        
        long days = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
        
        BigDecimal dailyBurn = 
            totalSpent.divide(BigDecimal.valueOf(days),
            2,
            RoundingMode.HALF_UP
        );

        int daysUntilDeplete =
            dailyBurn.compareTo(BigDecimal.ZERO) <= 0
            ? -1
            : balance.divide(dailyBurn, 0, RoundingMode.DOWN).intValue();

        return new BurnRateResponse(
            totalSpent,
            dailyBurn, 
            dailyBurn.multiply(BigDecimal.valueOf(7)), 
            dailyBurn.multiply(BigDecimal.valueOf(30)),
            daysUntilDeplete
        ); 
    } // getBurnRate

    public List<SpendingTrendResponse> getSpendingTrend(
        long userId,
        YearMonth startDate,
        YearMonth endDate,
        Long accountId,
        Long categoryId
    ) {
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        } // if

        if(accountId != null) {
            // check account belongs to user 
            accountRepo.findByUserIdAndAccountId(userId, accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        } // if

        if(categoryId != null) {
            // check category belongs to user
            categoryRepo.findByCategoryIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        } // if

        List<SpendingTrendResponse> responses = new ArrayList<>();

        LocalDate cur = startDate.atDay(1);
        BigDecimal prevTotal = BigDecimal.ZERO;

        while(!(cur.isAfter(endDate.atEndOfMonth()))) {
            BigDecimal totalSpent = transactionRepo.totalSpent(
                userId, 
                accountId, 
                categoryId, 
                cur,
                cur.with(TemporalAdjusters.lastDayOfMonth())
            );

            BigDecimal changeAmount = prevTotal == BigDecimal.ZERO 
                ?  BigDecimal.ZERO
                : totalSpent.subtract(prevTotal);

            double percentChange = 0;
            if(prevTotal.compareTo(BigDecimal.ZERO) > 0) {
                percentChange = changeAmount
                    .multiply(BigDecimal.valueOf(100))
                    .divide(prevTotal, 2, RoundingMode.HALF_UP)
                    .doubleValue();
                    
            }
            responses.add(new SpendingTrendResponse(
                YearMonth.from(cur),
                totalSpent,
                changeAmount,
                percentChange
            ));
            
            prevTotal = totalSpent;

            cur = cur.plusMonths(1); 
        } // while

        return responses;
    } // getSpendingTrend

    public RecurringTransactionResponse getRecurringTransactions(
        long userId,
        Long accountId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        } // if

        if(accountId != null) {
            // check account belongs to user 
            accountRepo.findByUserIdAndAccountId(userId, accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        } // if

        
        return null;
    } // getRecurringTransactions
    
} // AnalyticsService
