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

    public Account addAccount(long userId, Type type, BigDecimal balance) {
        Account account = new Account(userId, type, balance);
       return accountRepo.saveAccount(account);
    } // addAccount

} // AccountService
