package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.enums.TransactionType;
import com.kaylaarthur.financeapi.model.Account;
import com.kaylaarthur.financeapi.model.Transaction;
import com.kaylaarthur.financeapi.repository.AccountRepo;
import com.kaylaarthur.financeapi.repository.CategoryRepo;
import com.kaylaarthur.financeapi.repository.TransactionRepo;
import com.kaylaarthur.financeapi.request.AddTransactionRequest;
import com.kaylaarthur.financeapi.request.UpdateTransactionRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
                throw new RuntimeException("Expence results in a negative balance");
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


    @Transactional
    public Transaction updateTransaction(long userId, long transactionId, UpdateTransactionRequest request) {
        // check transaction belongs to user
        Transaction transaction = transactionRepo.findByUserIdAndTransactionId(userId, transactionId).orElseThrow(() -> new RuntimeException("Transaction does not belong to user"));
        // find account transaction belongs to 
        Account account = accountRepo.findByUserIdAndAccountId(userId, transaction.getAccountId()).orElseThrow(() -> new RuntimeException("Account for transaction not found"));
        // check account belongs to user
        if(account.getUserId() != userId) {
            throw new RuntimeException("Account does not belong to user");
        } // if

        BigDecimal newAmount = request.getAmount() != null ? request.getAmount() : transaction.getAmount();
        TransactionType newType = request.getTransactionType() != null ? request.getTransactionType() : transaction.getTransactionType();
        
        // undo old values
        if(transaction.getTransactionType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } // if

        // apply new values
        if(newType == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().subtract(newAmount));
        } else {
            account.setBalance(account.getBalance().add(newAmount));
        } // if 

        accountRepo.update(account); // update in database

        if(request.getAmount() != null) { transaction.setAmount(request.getAmount()); } // if
        if(request.getDate() != null) { transaction.setDate(request.getDate()); } // if
        if(request.getDescription() != null) { transaction.setDescription(request.getDescription()); } // if
        if(request.getTransactionType() != null) { transaction.setTransactionType(request.getTransactionType()); } // if

        // add transaction to database
        return transactionRepo.update(transaction);
    } // updateTransaction

    @Transactional
    public void deleteTransaction(long userId, long transactionId) {
        // check transaction belongs to user
        Transaction transaction = transactionRepo.findByUserIdAndTransactionId(userId, transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        // find account transaction belongs to 
        Account account = accountRepo.findByUserIdAndAccountId(userId, transaction.getAccountId()).orElseThrow(() -> new RuntimeException("Account for transaction not found"));

        // undo old values
        if(transaction.getTransactionType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } // if
        
        accountRepo.update(account); // update in database

        transactionRepo.delete(transactionId);
    } // deleteTransaction


    public Transaction getTransaction(long userId, long transactionId) {
        return transactionRepo.findByUserIdAndTransactionId(userId, transactionId).orElseThrow(() -> new RuntimeException("Couldn't get transaction for user"));
    } // getTransaction

    public List<Transaction> getAllTransactions(long userId, long accountId, long categoryId, TransactionType type, LocalDate startDate, LocalDate endDate) {
        return transactionRepo.findAllTransactions(userId, accountId, categoryId, type, startDate, endDate);
    } // getAllTransactions
    
} // TransactionService
