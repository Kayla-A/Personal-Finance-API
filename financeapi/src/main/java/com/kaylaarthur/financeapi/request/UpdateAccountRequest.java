package com.kaylaarthur.financeapi.request;

import com.kaylaarthur.financeapi.enums.Type;

import java.math.BigDecimal;

public class UpdateAccountRequest {
    
    private String name;
    private Type type;
    private BigDecimal balance;
    
    public UpdateAccountRequest(String name, Type type, BigDecimal balance) {
        this.name = name;
        this.type = type;
        this.balance = balance;
    } // UpdateAccountResponse

    public UpdateAccountRequest(String name) {
        this.name = name;
    } // UpdateAccountResponse

    public UpdateAccountRequest(Type type) {
        this.type = type;
    } // UpdateAccountResponse

    public UpdateAccountRequest(BigDecimal balance) {
        this.balance = balance;
    } // UpdateAccountResponse

    public String getName() { return name; } // getName

    public void setName(String name) { this.name = name; } // setName

    public Type getType() { return type; } // getType

    public void setType(Type type) { this.type = type; } // setType

    public BigDecimal getBalance() { return balance; } // getBalance

    public void setBalance(BigDecimal balance) { this.balance = balance; } // setBalance

} // UpdateAccountRequest
