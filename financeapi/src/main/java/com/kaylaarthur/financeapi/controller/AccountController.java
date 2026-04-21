package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.request.AddAccountRequest;
import com.kaylaarthur.financeapi.response.AddAccountResponse;
import com.kaylaarthur.financeapi.service.AccountService;
import com.kaylaarthur.financeapi.utility.SecurityUtility;
import com.kaylaarthur.financeapi.model.Account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    
    private final AccountService accountService;
    private final SecurityUtility securityUtility;

    public AccountController(AccountService accountService, SecurityUtility securityUtility) {
        this.accountService = accountService;
        this.securityUtility = securityUtility;
    } // AccountController

    @PostMapping("/add")
    public ResponseEntity<AddAccountResponse> addAccount(@RequestBody AddAccountRequest request) {
        securityUtility.getCurrentUser(); // get user context???
        Account account = accountService.addAccount(request.getUserId(), request.getType(), request.getBalance());
        AddAccountResponse response = new AddAccountResponse(account.getAccountId(), account.getUserId(), account.getType(), account.getBalance());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } // addAccount
    


} // AccountController
