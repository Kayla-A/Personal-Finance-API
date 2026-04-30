package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.request.AddBudgetRequest;
import com.kaylaarthur.financeapi.request.UpdateBudgetRequest;
import com.kaylaarthur.financeapi.response.BudgetResponse;
import com.kaylaarthur.financeapi.enums.BudgetInterval;
import com.kaylaarthur.financeapi.model.Budget;
import com.kaylaarthur.financeapi.service.BudgetService;
import com.kaylaarthur.financeapi.utility.SecurityUtility;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.ArrayList;

import java.math.BigDecimal;


@RestController
@RequestMapping("/budgets")
public class BudgetController {
    
    private SecurityUtility securityUtility;
    private BudgetService budgetService;
    
    public BudgetController(SecurityUtility securityUtility, BudgetService budgetService) {
        this.securityUtility = securityUtility;
        this.budgetService = budgetService;
    } // BudgetController

    @PostMapping
    public ResponseEntity<BudgetResponse> addBudget(@RequestBody AddBudgetRequest request) {
        User user = securityUtility.getCurrentUser();
        Budget budget = budgetService.addBudget(user.getId(), request);
        BudgetResponse response = mapToResponse(budget);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } // addBudget
    
    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(@PathVariable long id, @Valid @RequestBody UpdateBudgetRequest request) {
        User user = securityUtility.getCurrentUser();
        Budget budget = budgetService.updateBudget(user.getId(), id, request);
        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(budget));
    } // updateBudget
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable long id) {
        User user = securityUtility.getCurrentUser();
        budgetService.deleteBudget(user.getId(), id);
        return ResponseEntity.noContent().build();
    } // deleteBudget

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudget(@PathVariable long id) {
        User user = securityUtility.getCurrentUser();
        Budget budget = budgetService.getBudget(user.getId(), id);
        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(budget));
    } // getBudget

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) BigDecimal minLimit,
        @RequestParam(required = false) BigDecimal maxLimit,
        @RequestParam(required = false) BudgetInterval period
    ) {
        User user = securityUtility.getCurrentUser();
        List<Budget> budgets = budgetService.getAllBudgets(user.getId(), categoryId, minLimit, maxLimit, period);
        List<BudgetResponse> responses = new ArrayList<>();
        for(Budget budget : budgets) {
            responses.add(mapToResponse(budget));
        } // for
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    } // getAllBudgets
    
    private BudgetResponse mapToResponse(Budget budget) {
        return new BudgetResponse(
                    budget.getBudgetId(),
                    budget.getUserId(),
                    budget.getCategoryId(),
                    budget.getBudgetLimit(),
                    budget.getPeriod()
                );
    } // mapToResponse
    
} // BudgetController
