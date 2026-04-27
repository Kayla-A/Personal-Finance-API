package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;

import com.kaylaarthur.financeapi.enums.BudgetInterval;

public class BudgetResponse {
    
    private long budgetId;
    private long userId;
    private long categoryId;
    private BigDecimal budgetLimit;
    private BudgetInterval period;
   
    public BudgetResponse(long budgetId, long userId, long categoryId, BigDecimal budgetLimit, BudgetInterval period) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.budgetLimit = budgetLimit;
        this.period = period;
    } // BudgetResponse

    public long getBudgetId() {  return budgetId; }

    public long getUserId() { return userId; }

    public long getCategoryId() { return categoryId; }

    public BigDecimal getBudgetLimit() { return budgetLimit; }

    public BudgetInterval getPeriod() { return period; }

} // BudgetResponse
