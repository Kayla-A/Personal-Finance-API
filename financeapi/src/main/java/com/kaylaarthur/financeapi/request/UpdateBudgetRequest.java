package com.kaylaarthur.financeapi.request;

import java.math.BigDecimal;

import com.kaylaarthur.financeapi.enums.BudgetInterval;

import jakarta.validation.constraints.DecimalMin;

public class UpdateBudgetRequest {
    
    private Long categoryId;
    @DecimalMin("0.01")
    private BigDecimal budgetLimit;
    private BudgetInterval period;
    
    public UpdateBudgetRequest() {} // UpdateBudgetResquest

    public Long getCategoryId() { return categoryId; }

    public void setCategoryId(long categoryId) { this.categoryId = categoryId; }

    public BigDecimal getBudgetLimit() { return budgetLimit; }

    public void setBudgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; }

    public BudgetInterval getPeriod() { return period; }

    public void setPeriod(BudgetInterval period) { this.period = period; }

} // UpdateBudgetResquest
