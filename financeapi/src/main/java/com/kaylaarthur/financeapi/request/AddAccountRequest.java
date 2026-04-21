package com.kaylaarthur.financeapi.request;

import com.kaylaarthur.financeapi.enums.Type;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class AddAccountRequest {
    
    private long userId;
    @NotBlank
    private Type type;
    @NotBlank
    private BigDecimal balance;
    
    public AddAccountRequest() {} // AddAccountRequest

    public long getUserId() { return userId; } // getUserId

    public void setUserId(long userId) { this.userId = userId; } // setUserId

    public Type getType() { return type; } // getType

    public void setType(Type type) { this.type = type; } // setType

    public BigDecimal getBalance() { return balance; } // getBalance

    public void setBalance(BigDecimal balance) { this.balance = balance; } // setBalance

} // AddAccountRequest
