package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.service.TransactionService;
import com.kaylaarthur.financeapi.utility.SecurityUtility;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private SecurityUtility securityUtility;
    private TransactionService transactionService;
    
    public TransactionController(SecurityUtility securityUtility, TransactionService transactionService) {
        this.securityUtility = securityUtility;
        this.transactionService = transactionService;
    } // TransactionController

/*
    @PostMapping()
    public ResponseEntity<AddTransactionResponse> addTransaction(@RequestBody AddResponseRequest request) {
        //TODO: process POST request
        
        return ResponseEntity.noContent().build();
    } // addTransaction

    @PutMapping("/{id}")
    public ResponsEntity<UpdateTransactionResponse> updateTransaction(@PathVariable long id, @RequestBody UpdateTransactionRequest request) {
    } // updateTransaction

    @DeleteMapping("/{id}")
    public ResponsEntity<Void> deleteTransaction(@PathVariable long id) {
    } // deleteTransaction

    @GetMapping("/{id}")
    public ResponseEntity<GetTransactionResponse> getTransaction(@PathVariable long id, @RequestBody GetTransactionRequest request) {
    } // getTransaction

    @GetMapping("?accountId=1")
    public ResponseEntity<List<GetTransactionResponse>> getAllTransactions(@RequestParam(required = false) long accountId) {
    } // getAllTransactions
*/

} // TransactionController
