package com.kaylaarthur.financeapi.model;

import jakarta.persistence.Entity;
import com.kaylaarthur.financeapi.enums.Type;
import jakarta.persistence.Id;
import java.math.BigDecimal;

/**
 * 
 */
@Entity
public class Account {

    @Id
    private long accountId;
    private long userId;
    private Type type;
    private BigDecimal balance;

    /**
     * 
     * @param account_id
     * @param user_id
     * @param type
     * @param balance
     */
    public Account(long accountId, long userId, Type type, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.type = type;
        this.balance = balance;
    } // Account

    /**
     * 
     * @param userId
     * @param type
     * @param balance
     */
    public Account(long userId, Type type, BigDecimal balance) {
        this.userId = userId;
        this.type = type;
        this.balance = balance;
    } // Account

    public long getAccountId() { return accountId; } // getAccountId

    public void setAccountId(long accountId) { this.accountId = accountId; } // setAccountId

    public long getUserId() { return userId; } // getUserId

    public void setUserId(long userId) { this.userId = userId; } // setUserId

    public Type getType() { return type; } // getType

    public void setType(Type type) { this.type = type; } // setType

    public BigDecimal getBalance() { return balance; } // getBalanc

    public void setBalance(BigDecimal balance) { this.balance = balance; } // setBalance

    
} // Account
