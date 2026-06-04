package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.response.BudgetOverrunResponse;
import com.kaylaarthur.financeapi.response.BudgetUsageResponse;
import com.kaylaarthur.financeapi.response.BurnRateResponse;
import com.kaylaarthur.financeapi.response.CategorySpendingResponse;
import com.kaylaarthur.financeapi.response.MonthlySummaryResponse;
import com.kaylaarthur.financeapi.response.SpendingTrendResponse;
import com.kaylaarthur.financeapi.service.AnalyticsService;
import com.kaylaarthur.financeapi.utility.SecurityUtility;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.YearMonth;

import java.util.List;


@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    
    private SecurityUtility securityUtility;

    private AnalyticsService analyticsService;

    public AnalyticsController(SecurityUtility securityUtility, AnalyticsService analyticsService) {
        this.securityUtility = securityUtility;
        this.analyticsService = analyticsService;
    } // AnalyticsController

    @GetMapping("/budget-usage")
    public ResponseEntity<BudgetUsageResponse> getBudgetUsage(
        @RequestParam long categoryId, 
        @RequestParam BudgetInterval period
    ) {
        User user = securityUtility.getCurrentUser();
        BudgetUsageResponse response = analyticsService.getBudgetUsage(
            user.getId(), 
            categoryId, 
            period
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } // getBudgetUsage // categoryId and period
    
    
    @GetMapping("/monthly-summary") 
    public ResponseEntity<List<MonthlySummaryResponse>> getMonthlySummary(
        @RequestParam YearMonth startDate,
        @RequestParam YearMonth endDate
    ) {
        User user = securityUtility.getCurrentUser();
        List<MonthlySummaryResponse> responses = analyticsService.getMonthlySummary(
            user.getId(), 
            startDate, 
            endDate
        );
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    } // getMonthlySummary
     
    @GetMapping("/category-spending")
    public ResponseEntity<List<CategorySpendingResponse>> getSpendingByCategory(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        @RequestParam (required = false) Long accountId,
        @RequestParam (required = false) BigDecimal minAmount
    ) {
        User user = securityUtility.getCurrentUser();
        List<CategorySpendingResponse> responses = analyticsService.getSpendingByCategory(
            user.getId(), 
            startDate, 
            endDate,
            accountId,
            minAmount
        );
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    } // getSpendingByCategory

    @GetMapping("/budget-overrun")
    public ResponseEntity<List<BudgetOverrunResponse>> getBudgetOverrun() {
        User user = securityUtility.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK)
                .body(analyticsService.getBudgetOverrun(user.getId()));
    } // getBudgetOverrun
    
    @GetMapping("/burn-rate")
    public ResponseEntity<BurnRateResponse> getBurnRate(
        @RequestParam(required = false) Long accountId,
        @RequestParam(required = false) Long categoryId,
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate
    ) {
        User user = securityUtility.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(
            analyticsService.getBurnRate(
                user.getId(), 
                accountId, 
                categoryId, 
                startDate, 
                endDate
            ));
    } // getBurnRate
    
    @GetMapping("spending-trend")
    public ResponseEntity<List<SpendingTrendResponse>> getSpendingTrend(
        @RequestParam YearMonth startDate,
        @RequestParam YearMonth endDate,
        @RequestParam(required = false) Long accountId,
        @RequestParam(required = false) Long categoryId
    ) {
        User user = securityUtility.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(
            analyticsService.getSpendingTrend(
                user.getId(),
                startDate,
                endDate,
                accountId,
                categoryId
            )
        );
    } // getSpendingTrend

    /*
    public getCategoryDistribution() {}
    public getRecurringTransactions() {}
    public getAnomalies() {}
    public getForcasted() {} // category
    public getSavingRate() {}
    public getTopCategories() {} // add a limit
    public getAverageSpending() {} // by interval
    public getTotalSpending() {} // date range
    public getIncomeVsExpense() {} // date range
    */

} // ComputationController
