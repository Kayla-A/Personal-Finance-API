package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;

public class CategorySpendingResponse {

    private String categoryName;
    private BigDecimal totalSpent;
    private double percentOfTotalSpending;
    private BigDecimal averageTransactionSize;
    
    public CategorySpendingResponse(String categoryName, BigDecimal totalSpent, double percentOfTotalSpending, BigDecimal averageTransactionSize) {
        this.categoryName = categoryName;
        this.totalSpent = totalSpent;
        this.percentOfTotalSpending = percentOfTotalSpending;
        this.averageTransactionSize = averageTransactionSize;
    } // CategorySpendingResponse

    public String getCategoryName() { return categoryName; } // getCategoryName

    public BigDecimal getTotalSpent() { return totalSpent; } // getTotalSpent

    public double getPercentOfTotalSpending() { return percentOfTotalSpending; } // getPercentOfTotalSpending

    public BigDecimal getAverageTransactionSize() { return averageTransactionSize; } // getAverageTransactionSize

} // CategorySpendingResponse
