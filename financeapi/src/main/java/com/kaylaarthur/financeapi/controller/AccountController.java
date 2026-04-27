package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.request.AddAccountRequest;
import com.kaylaarthur.financeapi.request.UpdateAccountRequest;
import com.kaylaarthur.financeapi.response.AccountResponse;
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

import java.util.List;
import java.util.ArrayList;


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
    public ResponseEntity<AccountResponse> addAccount(@Valid @RequestBody AddAccountRequest request) {
        User user = securityUtility.getCurrentUser(); 
        Account account = accountService.addAccount(user.getId(), request.getName(), request.getType(), request.getBalance());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(account));
    } // addAccount

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        User user = securityUtility.getCurrentUser();
        List<Account> accounts = accountService.getAllAccounts(user.getId());
        List<AccountResponse> response = new ArrayList<>();
        for(Account account : accounts) {
            response.add(mapToResponse(account));
        } // for each
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } // getAllAccounts

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable long id) {
        User user = securityUtility.getCurrentUser();
        Account account = accountService.getAccount(user.getId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(account));
    } // getAccount

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable long id, @RequestBody UpdateAccountRequest request) {
        User user = securityUtility.getCurrentUser();
        Account account = accountService.updateAccount(id, user.getId(), request.getName(), request.getType(), request.getBalance());
        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(account));
    } // updateAccount

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable long id) {
        User user = securityUtility.getCurrentUser();
        accountService.deleteAccount(user.getId(), id);
        return ResponseEntity.noContent().build();
    } // deleteAccount    
    
    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
            account.getAccountId(),
            account.getUserId(),
            account.getName(),
            account.getType(),
            account.getBalance()
        );
    } // mapToResponse 
    
} // AccountController
