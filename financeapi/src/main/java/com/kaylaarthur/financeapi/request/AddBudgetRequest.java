package com.kaylaarthur.financeapi.request;

import java.math.BigDecimal;

import com.kaylaarthur.financeapi.enums.BudgetInterval;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AddBudgetRequest {
   
    @NotNull
    private Long categoryId;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal budgetLimit;
    private BudgetInterval period;
   
    public AddBudgetRequest() {} // AddBudgetRequest

    public long getCategoryId() {  return categoryId; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public BigDecimal getBudgetLimit() { return budgetLimit; }

    public void setBudgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; }

    public BudgetInterval getPeriod() { return period; }

    public void setPeriod(BudgetInterval period) { this.period = period; }

} // AddBudgetRequest
