package com.kaylaarthur.financeapi.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.kaylaarthur.financeapi.enums.TransactionType;

public class AddTransactionResponse {
    
    private long transactionId;
    private long categoryId;
    private long accountId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private TransactionType transactionType;
    
    public AddTransactionResponse(long transactionId, long categoryId, long accountId, BigDecimal amount,
            LocalDate date, String description, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
    } // AddTransactionResponse

    public long getTransactionId() { return transactionId; } // getTransactionId

    public long getCategoryId() { return categoryId; } // getCategoryId

    public long getAccountId() { return accountId; } // getAccountId

    public BigDecimal getAmount() { return amount; } // getAmount

    public LocalDate getDate() { return date; } // getDate

    public String getDescription() { return description; } // getDescription

    public TransactionType getTransactionType() { return transactionType; } // getTransactionType

} // AddTransactionResponse
