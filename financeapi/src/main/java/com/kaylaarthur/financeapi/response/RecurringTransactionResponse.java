package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecurringTransactionResponse {
    private String categoryName;
    private BigDecimal averageAmount;
    private String frequency;
    private LocalDate nextExpectedDate;

    
    public RecurringTransactionResponse(String categoryName, BigDecimal averageAmount,
            String frequency, LocalDate nextExpectedDate) {
        this.categoryName = categoryName;
        this.averageAmount = averageAmount;
        this.frequency = frequency;
        this.nextExpectedDate = nextExpectedDate;
    } // RecurringTransactionResponse

    public String getCategoryName() { return categoryName; } // getCategoryName

    public BigDecimal getAverageAmount() { return averageAmount; } // getAverageAmount

    public String getFrequency() { return frequency; } // getFrequency

    public LocalDate getNextExpectedDate() { return nextExpectedDate; } // getNextExpectedDate

} // RecurringTransactionResponse
