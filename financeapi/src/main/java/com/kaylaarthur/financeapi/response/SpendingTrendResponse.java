package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;
import java.time.YearMonth;

public class SpendingTrendResponse {
    private YearMonth period; // year-month
    private BigDecimal totalSpent;
    private BigDecimal changeAmount;
    private double percentChange;
    
    public SpendingTrendResponse(YearMonth period, BigDecimal totalSpent, BigDecimal changeAmount, double percentChange) {
        this.period = period;
        this.totalSpent = totalSpent;
        this.changeAmount = changeAmount;
        this.percentChange = percentChange;
    } // SpendingTrendResponse

    public YearMonth getPeriod() {  return period; } // getPeriod

    public void setPeriod(YearMonth period) { this.period = period; } // setPeriod

    public BigDecimal getTotalSpent() { return totalSpent; } // getTotalSpent

    public void setTotalSpent(BigDecimal totalSpent) {  this.totalSpent = totalSpent; } // setTotalSpent

    public BigDecimal getChangeAmount() { return changeAmount; } // getChangeAmount

    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; } // setChangeAmount

    public double getPercentChange() { return percentChange; } // getPercentChange

    public void setPercentChange(double percentChange) {  this.percentChange = percentChange; } // setPercentChange

} // SpendingTrendResponse
