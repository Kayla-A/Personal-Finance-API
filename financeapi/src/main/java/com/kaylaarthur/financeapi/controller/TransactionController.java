package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.request.AddTransactionRequest;
import com.kaylaarthur.financeapi.response.AddTransactionResponse;
import com.kaylaarthur.financeapi.request.UpdateTransactionRequest;
import com.kaylaarthur.financeapi.response.UpdateTransactionResponse;
import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.model.Transaction;
import com.kaylaarthur.financeapi.service.TransactionService;
import com.kaylaarthur.financeapi.utility.SecurityUtility;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.ArrayList;



@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private SecurityUtility securityUtility;
    private TransactionService transactionService;
    
    public TransactionController(SecurityUtility securityUtility, TransactionService transactionService) {
        this.securityUtility = securityUtility;
        this.transactionService = transactionService;
    } // TransactionController


    @PostMapping()
    public ResponseEntity<AddTransactionResponse> addTransaction(@RequestBody AddTransactionRequest request) {
        User user = securityUtility.getCurrentUser();
        Transaction transaction = transactionService.addTransaction(user.getId(), request);
        AddTransactionResponse response = new AddTransactionResponse(
            transaction.getTransactionId(),
            transaction.getCategoryId(),
            transaction.getAccountId(), 
            transaction.getAmount(), 
            transaction.getDate(), 
            transaction.getDescription(), 
            transaction.getTransactionType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } // addTransaction
 
    @PutMapping("/{id}")
    public ResponseEntity<UpdateTransactionResponse> updateTransaction(@PathVariable long id, @RequestBody UpdateTransactionRequest request) {
        User user = securityUtility.getCurrentUser();
        Transaction transaction = transactionService.updateTransaction(user.getId(), id, request);
        UpdateTransactionResponse response = new UpdateTransactionResponse(
            transaction.getTransactionId(),
            transaction.getCategoryId(),
            transaction.getAccountId(), 
            transaction.getAmount(), 
            transaction.getDate(), 
            transaction.getDescription(), 
            transaction.getTransactionType()
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } // updateTransaction

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable long id) {
        User user = securityUtility.getCurrentUser();
        transactionService.deleteTransaction(user.getId(), id);
        return ResponseEntity.noContent().build();
    } // deleteTransaction
/*
    @GetMapping("/{id}")
    public ResponseEntity<GetTransactionResponse> getTransaction(@PathVariable long id, @RequestBody GetTransactionRequest request) {
    } // getTransaction

    @GetMapping("?accountId=1")
    public ResponseEntity<List<GetTransactionResponse>> getAllTransactions(@RequestParam(required = false) long accountId) {
    } // getAllTransactions
*/

} // TransactionController
