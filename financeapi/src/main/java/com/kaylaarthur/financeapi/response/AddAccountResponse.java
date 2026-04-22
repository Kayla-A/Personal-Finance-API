package com.kaylaarthur.financeapi.response;

import com.kaylaarthur.financeapi.enums.Type;
import java.math.BigDecimal;

public class AddAccountResponse {

    private long accountId;
    private long userId;
    private String name;
    private Type type;
    private BigDecimal balance;

    public AddAccountResponse(long accountId, long userId, String name, Type type, BigDecimal balance) {
        this.name = name;
        this.accountId = accountId;
        this.userId = userId;
        this.type = type;
        this.balance = balance;
    } // AddAccountResponse

    public String getName() { return name; } // getName

    public long getAccountId() { return accountId; } // getAccountId

    public long getUserId() { return userId; } // getUserId

    public Type getType() { return type; } // getType

    public BigDecimal getBalance() { return balance; } // getBalance
    
} // AddAccountResponse
