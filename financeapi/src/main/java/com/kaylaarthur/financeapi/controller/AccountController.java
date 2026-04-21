package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.request.AddAccountRequest;
import com.kaylaarthur.financeapi.response.AddAccountResponse;
import com.kaylaarthur.financeapi.service.AccountService;
import com.kaylaarthur.financeapi.utility.SecurityUtility;
import com.kaylaarthur.financeapi.model.Account;
import com.kaylaarthur.financeapi.model.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/accounts")
public class AccountController {
    
    private final AccountService accountService;
    private final SecurityUtility securityUtility;

    public AccountController(AccountService accountService, SecurityUtility securityUtility) {
        this.accountService = accountService;
        this.securityUtility = securityUtility;
    } // AccountController

    @PostMapping()
    public ResponseEntity<AddAccountResponse> addAccount(@Valid @RequestBody AddAccountRequest request) {
        User user = securityUtility.getCurrentUser(); 
        Account account = accountService.addAccount(user.getId(), request.getName(), request.getType(), request.getBalance());
        AddAccountResponse response = new AddAccountResponse(account.getAccountId(), user.getId(), account.getName(), account.getType(), account.getBalance());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } // addAccount

    @GetMapping()
    public String getAllAccounts() {
        return new String();
    } // getAllAccounts

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable long accountId) {
        User user = securityUtility.getCurrentUser();
        Account account = accountService.getAccount(user.getId(), accountId);
        return account;
    } // getAccount

    @PutMapping("/{id}")
    public String updateAccount(@PathVariable String id, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    } // updateAccount

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable long accountId) {
        User user = securityUtility.getCurrentUser();
        accountService.deleteAccount(user.getId(), accountId);
        return ResponseEntity.noContent().build();
    } // deleteAccount    
    
} // AccountController
