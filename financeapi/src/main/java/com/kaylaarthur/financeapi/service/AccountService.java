package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.model.Account;
import com.kaylaarthur.financeapi.enums.Type;
import com.kaylaarthur.financeapi.repository.AccountRepo;

import java.math.BigDecimal;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    private final AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    } // AccountService

    public Account addAccount(long userId, String name, Type type, BigDecimal balance) {
        accountRepo.findByUserIdAndName(userId, name).ifPresent(u -> {throw new IllegalArgumentException("Account name already exists for user"); });
        Account account = new Account(userId, name, type, balance);
        return accountRepo.save(account);
    } // addAccount
    
    public List<Account> getAllAccounts(long userId) {
        return accountRepo.findAccountsByUserId(userId);
    } // getAllAccounts

    public Account getAccount(long userId, long accountId) {
        return accountRepo.findByUserIdAndAccountId(userId, accountId).orElseThrow(() -> new RuntimeException("Account not found"));
    } // getAccount

    public Account updateAccount(long accountId, long userId, String name, Type type, BigDecimal balance) {
        accountRepo.findByUserIdAndAccountId(userId, accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        Account account = new Account(accountId, userId);
        
        if(name != null) {
            accountRepo.findByUserIdAndName(userId, name).ifPresent(u -> {throw new IllegalArgumentException("Account name already exists for user"); });
            account.setName(name);
        } // if 
        if(type != null) {
            account.setType(type);
        } // if 
        if(balance != null){
            account.setBalance(balance);
        } // if 
        
        return accountRepo.update(account);
    } // updateAccount

    public void deleteAccount(long userId, long accountId) {
        accountRepo.findByUserIdAndAccountId(userId, accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepo.delete(userId, accountId);
    } // deleteAccount

} // AccountService
