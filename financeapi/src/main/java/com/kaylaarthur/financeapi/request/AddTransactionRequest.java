package com.kaylaarthur.financeapi.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.kaylaarthur.financeapi.enums.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AddTransactionRequest {
    
    @NotNull
    private long categoryId;
    @NotNull
    private long accountId;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
    @NotNull
    private LocalDate date;
    private String description;
    @NotNull
    private TransactionType transactionType;
   
    public AddTransactionRequest() {} // AddTransactionRequest

    public long getCategoryId() { return categoryId; } // getCategoryId

    public void setCategoryId(long categoryId) { this.categoryId = categoryId; } // setCategoryId

    public long getAccountId() { return accountId; } // getAccountId

    public void setAccountId(long accountId) { this.accountId = accountId; } // setAccountId

    public BigDecimal getAmount() { return amount; } // getAmount

    public void setAmount(BigDecimal amount) { this.amount = amount; } // setAmount

    public LocalDate getDate() { return date; } // getDat

    public void setDate(LocalDate date) { this.date = date; } // setDate

    public String getDescription() { return description; } // getDescription

    public void setDescription(String description) { this.description = description; } // setDescription

    public TransactionType getTransactionType() { return transactionType; } // getTransactionType

    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; } // setTransactionType

} // AddTransactionRequest
