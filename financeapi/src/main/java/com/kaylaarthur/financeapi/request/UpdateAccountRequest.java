package com.kaylaarthur.financeapi.request;

import com.kaylaarthur.financeapi.enums.Type;

import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateAccountRequest {
    
    @Size(max=20)
    private String name;
    private Type type;
    private BigDecimal balance;

    public UpdateAccountRequest() {} // UpdateAccountRequest

    public String getName() { return name; } // getName

    public void setName(String name) { this.name = name; } // setName

    public Type getType() { return type; } // getType

    public void setType(Type type) { this.type = type; } // setType

    public BigDecimal getBalance() { return balance; } // getBalance

    public void setBalance(BigDecimal balance) { this.balance = balance; } // setBalance

} // UpdateAccountRequest
