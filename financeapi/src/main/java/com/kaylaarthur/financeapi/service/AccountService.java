package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.model.Account;
import com.kaylaarthur.financeapi.enums.Type;
import com.kaylaarthur.financeapi.repository.AccountRepo;

import java.math.BigDecimal;

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



    public void deleteAccount(long userId, long accountId) {
        accountRepo.findByUserIdAndAccountId(userId, accountId).orElseThrow(() -> new RuntimeException("Could not find account"));
        accountRepo.delete(userId, accountId);
    } // deleteAccount

} // AccountService
