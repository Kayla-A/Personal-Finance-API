package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BudgetUsageResponse {
    
    private long categoryId;
    private BigDecimal limit;
    private BigDecimal spent;
    private BigDecimal remaining;
    private BigDecimal percentUsed;
    
    public BudgetUsageResponse(long categoryId, BigDecimal limit, BigDecimal spent) {
        this.categoryId = categoryId;
        this.limit = limit;
        this.spent = spent;

        remaining = limit.subtract(spent);

        if(limit.compareTo(BigDecimal.ZERO) > 0) {
            percentUsed = spent.multiply(BigDecimal.valueOf(100)).divide(limit, 4, RoundingMode.HALF_UP);
        } else {
            percentUsed = BigDecimal.ZERO;
        } // if
    } // BudgetUsageResponse

    public long getCategoryId() { return categoryId; } // getCategoryId
    
    public BigDecimal getLimit() { return limit; } // getLimit
   
    public BigDecimal getSpent() { return spent; } // getSpent

    public BigDecimal getRemaining() { return remaining; } // getRemaining

    public BigDecimal getPercentUsed() { return percentUsed; } // getPercentUsed

} // BudgetUsageResponse
