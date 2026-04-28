package com.kaylaarthur.financeapi.model;

import com.kaylaarthur.financeapi.enums.Type;

import jakarta.validation.constraints.Size;
import jakarta.persistence.Entity;
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
    @Size(max=20)
    private String name;
    private Type type;
    private BigDecimal balance;

    /**
     * 
     * @param accountId
     * @param userId
     * @param name
     * @param type
     * @param balance
     */
    public Account(long accountId, long userId, String name, Type type, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.balance = balance;
    } // Account

    /**
     * 
     * @param name
     * @param type
     * @param balance
     */
    public Account(long userId, String name, Type type, BigDecimal balance) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.balance = balance;
    } // Account

    /**
     * 
     * @param name
     * @param type
     * @param balance
     */
    public Account(long userId, String name) {
        this.userId = userId;
        this.name = name;
    } // Account

    /**
     * 
     * 
     * @param acountId
     * @param userId
     */
    public Account(long accountId, long userId) {
        this.accountId = accountId;
        this.userId = userId;
    } // Account

    public long getAccountId() { return accountId; } // getAccountId

    public void setAccountId(long accountId) { this.accountId = accountId; } // setAccountId

    public long getUserId() { return userId; } // getUserId

    public void setUserId(long userId) { this.userId = userId; } // setUserId

    public void setName(String name) { this.name = name; } // setName

    public String getName() { return name; } // getName

    public Type getType() { return type; } // getType

    public void setType(Type type) { this.type = type; } // setType

    public BigDecimal getBalance() { return balance; } // getBalanc

    public void setBalance(BigDecimal balance) { this.balance = balance; } // setBalance

    
} // Account
