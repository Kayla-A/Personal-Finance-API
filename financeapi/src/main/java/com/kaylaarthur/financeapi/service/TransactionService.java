package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.enums.TransactionType;
import com.kaylaarthur.financeapi.model.Account;
import com.kaylaarthur.financeapi.model.Transaction;
import com.kaylaarthur.financeapi.repository.AccountRepo;
import com.kaylaarthur.financeapi.repository.CategoryRepo;
import com.kaylaarthur.financeapi.repository.TransactionRepo;
import com.kaylaarthur.financeapi.request.AddTransactionRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    
    private TransactionRepo transactionRepo;
    private AccountRepo accountRepo;
    private CategoryRepo categoryRepo;

    public TransactionService(TransactionRepo transactionRepo, AccountRepo accountRepo, CategoryRepo categoryRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
    } // TransactionService

    @Transactional
    public Transaction addTransaction(long userId, AddTransactionRequest request) {
        // check account exist & belongs to user 
        Account account = accountRepo.findByUserIdAndAccountId(userId, request.getAccountId()).orElseThrow(() -> new RuntimeException("Account for transaction not found"));
        // check category exists
        categoryRepo.findByCategoryIdAndUserId(request.getCategoryId(), userId).orElseThrow(() -> new RuntimeException("Category for transaction not found"));

        // update account balance
        if(request.getTransactionType() == TransactionType.EXPENSE) {
            // check for negative balance 
            if(account.getBalance().compareTo(request.getAmount()) == -1) {
                throw new RuntimeException("Exspence results in a negative balance");
            } // if
            account.setBalance(account.getBalance().subtract(request.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(request.getAmount()));
        } // if

        // update in database
        accountRepo.update(account);

        // add transaction to database
        Transaction transaction = new Transaction(
            request.getCategoryId(), 
            request.getAccountId(), 
            request.getAmount(), 
            request.getDate(), 
            request.getDescription(), 
            request.getTransactionType()
        );

        return transactionRepo.save(transaction);
    } // addTransaction

    
} // TransactionService
