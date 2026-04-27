package com.kaylaarthur.financeapi.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.kaylaarthur.financeapi.enums.TransactionType;

import jakarta.validation.constraints.DecimalMin;

public class UpdateTransactionRequest {

    @DecimalMin("0.01")
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private TransactionType transactionType;

    public UpdateTransactionRequest() {} // UpdateTransactionRequest

    public BigDecimal getAmount() { return amount; } // getAmount

    public void setAmount(BigDecimal amount) { this.amount = amount; } // setAmount

    public LocalDate getDate() { return date; } // getDat

    public void setDate(LocalDate date) { this.date = date; } // setDate

    public String getDescription() { return description; } // getDescription

    public void setDescription(String description) { this.description = description; } // setDescription

    public TransactionType getTransactionType() { return transactionType; } // getTransactionType

    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; } // setTransactionType

} // UpdateTransactionRequest
