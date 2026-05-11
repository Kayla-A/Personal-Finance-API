package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;

public class MonthlySummaryResponse {

    private int year;
    private int month;
    private BigDecimal totalExpense;
    private BigDecimal totalIncome;
    private String mostFrequentCategory;
    
    public MonthlySummaryResponse(int year, int month, BigDecimal totalExpense, BigDecimal totalIncome, String mostFrequentCategory) {
        this.year = year;
        this.month = month;
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
        this.mostFrequentCategory = mostFrequentCategory;
    } // MonthlySummaryResponse

    

    public int getYear() { return year; } // getYear

    public int getMonth() { return month; } // getMonth

    public BigDecimal getTotalExpense() { return totalExpense; } // getTotalExpense

    public BigDecimal getTotalIncome() { return totalIncome; } // getTotalIncome

    public String getMostFrequentCategory() { return mostFrequentCategory; } // getMostFrequentCategory

} // MonthlySummaryResponse
