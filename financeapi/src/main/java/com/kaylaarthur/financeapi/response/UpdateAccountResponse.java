package com.kaylaarthur.financeapi.response;

import com.kaylaarthur.financeapi.enums.Type;

import java.math.BigDecimal;

public class UpdateAccountResponse {

    private long accountId;
    private long userId;
    private String name;
    private Type type;
    private BigDecimal balance;
    
    public UpdateAccountResponse(long accountId, long userId, String name, Type type, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.balance = balance;
    } // UpdateAccountResponse

    public UpdateAccountResponse(long accountId, long userId, String name) {
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
    } // UpdateAccountResponse

    public UpdateAccountResponse(long accountId, long userId, Type type) {
        this.accountId = accountId;
        this.userId = userId;
        this.type = type;
    } // UpdateAccountResponse

    public UpdateAccountResponse(long accountId, long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    } // UpdateAccountResponse

    public long getAccountId() { return accountId; } // getAccountId

    public long getUserId() { return userId; } // getUserId

    public String getName() { return name; } // getName

    public Type getType() { return type; } // getType

    public BigDecimal getBalance() { return balance; } // getBalance

} // UpdateAccountResponse
