package com.kaylaarthur.financeapi.model;

import com.kaylaarthur.financeapi.enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transaction {
    
    @Id
    private long transactionId;
    private long categoryId;
    private long accountId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private TransactionType transactionType;
    
    public Transaction(long transactionId, long categoryId, long accountId, BigDecimal amount, LocalDate date,
            String description, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
    } // Transaction

    

    public Transaction(long categoryId, long accountId, BigDecimal amount, LocalDate date, String description,
            TransactionType transactionType) {
        this.categoryId = categoryId;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
    } // Transaction



    public long getTransactionId() { return transactionId; } // getTransactionId

    public void setTransactionId(long transactionId) { this.transactionId = transactionId; } // setTransactionId

    public long getCategoryId() { return categoryId; } // getCategoryId

    public void setCategoryId(long categoryId) { this.categoryId = categoryId; } // setCategoryId

    public long getAccountId() { return accountId; } // getAccountId

    public void setAccountId(long accountId) { this.accountId = accountId; } // setAccountId

    public BigDecimal getAmount() { return amount; } // getAmount

    public void setAmount(BigDecimal amount) { this.amount = amount; } // setAmount

    public LocalDate getDate() { return date; } // getDate

    public void setDate(LocalDate date) { this.date = date; } // setDate

    public String getDescription() { return description; } // getDescription

    public void setDescription(String description) { this.description = description; } // setDescription

    public TransactionType getTransactionType() { return transactionType; } // getTransactionType

    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; } // setTransactionType

} // Transaction
