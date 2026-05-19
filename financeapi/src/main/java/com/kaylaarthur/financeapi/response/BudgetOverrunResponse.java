package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.kaylaarthur.financeapi.enums.BudgetInterval;

public class BudgetOverrunResponse {
    
    private String categoryName;
    private BigDecimal budgetLimit;
    private BigDecimal actualSpent;
    private BigDecimal percentOverBudget;
    private BudgetInterval period;
    
    public BudgetOverrunResponse(String categoryName, BigDecimal budgetLimit, BigDecimal actualSpent, BudgetInterval period) {
        this.categoryName = categoryName;
        this.budgetLimit = budgetLimit;
        this.actualSpent = actualSpent;
        this.percentOverBudget = actualSpent.subtract(budgetLimit)
            .divide(budgetLimit, 2, RoundingMode.HALF_UP)
            .multiply(new BigDecimal(100));
        this.period = period;
    } // BudgetOverrunResponse

    public String getCategoryName() { return categoryName; } // getCategoryName

    public BigDecimal getBudgetLimit() { return budgetLimit; } // getBudgetLimit

    public BigDecimal getActualSpent() { return actualSpent; } // getActualSpent

    public BigDecimal getPercentOverBudget() { return percentOverBudget; } // getPercentOverBudget

    public BudgetInterval getPeriod() { return period; } // getPeriod
    
} // BudgetOverrunResponse
