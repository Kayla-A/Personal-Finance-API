package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.response.BudgetUsageResponse;
import com.kaylaarthur.financeapi.service.AnalyticsService;
import com.kaylaarthur.financeapi.utility.SecurityUtility;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
        BudgetUsageResponse response = analyticsService.getBudgetUsage(user.getId(), categoryId, period);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } // getBudgetUsage // categoryId and period
    
    /*
    public getMonthlySummary() 
    public getSpendingByCategory() // date range
    public getBudgetOverrun() 
    public getBurnRate() // category
    public getSpendingTrend() {} // by category
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
