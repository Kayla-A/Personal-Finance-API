package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;

public class BurnRateResponse {
    private BigDecimal totalSpent;
    private BigDecimal averageDailyBurn;
    private BigDecimal averageWeeklyBurn;
    private BigDecimal averageMonthlyBurn;
    private int daysUntilFundsDeplete;
    
    public BurnRateResponse(BigDecimal totalSpent, BigDecimal averageDailyBurn, BigDecimal averageWeeklyBurn,
            BigDecimal averageMonthlyBurn, int daysUntilFundsDeplete) {
        this.totalSpent = totalSpent;
        this.averageDailyBurn = averageDailyBurn;
        this.averageWeeklyBurn = averageWeeklyBurn;
        this.averageMonthlyBurn = averageMonthlyBurn;
        this.daysUntilFundsDeplete = daysUntilFundsDeplete;
    } // BurnRateResponse

    public BigDecimal getTotalSpent() { return totalSpent; } // getTotalSpent

    public BigDecimal getAverageDailyBurn() { return averageDailyBurn; } // getAverageDailyBurn

    public BigDecimal getAverageWeeklyBurn() { return averageWeeklyBurn; } // getAverageWeeklyBurn

    public BigDecimal getAverageMonthlyBurn() { return averageMonthlyBurn; } // getAverageMonthlyBurn

    public int getDaysUntilFundsDeplete() {  return daysUntilFundsDeplete; } // getDaysUntilFundsDeplete

} // BurnRateResponse
