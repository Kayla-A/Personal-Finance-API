package com.kaylaarthur.financeapi.model;

import com.kaylaarthur.financeapi.enums.BudgetInterval;

import java.math.BigDecimal;

import jakarta.persistence.Id;

public class Budget {
    
    @Id
    private long budgetId;
    private long userId;
    private long categoryId;
    private BigDecimal budgetLimit;
    private BudgetInterval period;
    
    public Budget(long budgetId, long userId, long categoryId, BigDecimal budgetLimit, BudgetInterval period) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.budgetLimit = budgetLimit;
        this.period = period;
    } // Budget

    public Budget(long userId, long categoryId, BigDecimal budgetLimit, BudgetInterval period) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.budgetLimit = budgetLimit;
        this.period = period;
    } // Budget

    public long getBudgetId() { return budgetId; } // getBudgetId

    public void setBudgetId(long budgetId) { this.budgetId = budgetId; } // setBudgetId

    public long getUserId() { return userId; } // getUserId

    public void setUserId(long userId) { this.userId = userId; } // setUserId

    public long getCategoryId() { return categoryId; } // getCategoryId

    public void setCategoryId(long categoryId) { this.categoryId = categoryId; } // setCategoryId

    public BigDecimal getBudgetLimit() { return budgetLimit; } // getBudgetLimit

    public void setBudgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; } // setBudgetLimit

    public BudgetInterval getPeriod() { return period; } // getPeriod

    public void setPeriod(BudgetInterval period) { this.period = period; } // setPeriod

} // Budget
