package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.model.Category;
import com.kaylaarthur.financeapi.request.AddCategoryRequest;
import com.kaylaarthur.financeapi.response.CategoryResponse;
import com.kaylaarthur.financeapi.request.UpdateCategoryRequest;
import com.kaylaarthur.financeapi.service.CategoryService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaylaarthur.financeapi.utility.SecurityUtility;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

import java.util.List;
import java.util.ArrayList;


@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private CategoryService categoryService;
    private SecurityUtility securityUtility;
    
    public CategoryController(CategoryService categoryService, SecurityUtility securityUtility) {
        this.categoryService = categoryService;
        this.securityUtility = securityUtility;
    } // CategoryController

    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody AddCategoryRequest request) {
        User user = securityUtility.getCurrentUser();
        Category category = categoryService.addCategory(user.getId(), request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(category));
    } // addCategory

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable long id, @RequestBody UpdateCategoryRequest request) {
        User user = securityUtility.getCurrentUser();
        Category category = categoryService.updateCategory(id, user.getId(), request.getCategoryName());
        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(category));
    } // updateCategory

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        User user = securityUtility.getCurrentUser();
        categoryService.deleteCategory(id, user.getId());
        return ResponseEntity.noContent().build();
    } // deleteCategory

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable long id) {
        User user = securityUtility.getCurrentUser();
        Category category = categoryService.getCategory(id, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(category));
    } // getCategory

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        User user = securityUtility.getCurrentUser();
        List<Category> categories = categoryService.getAllCategories(user.getId());
        List<CategoryResponse> response = new ArrayList<>();
        for(Category category : categories) {
            response.add(mapToResponse(category));
        } // for each
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } // getAllCategories

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
            category.getCategoryId(),
            category.getUserId(),
            category.getCategoryName()
        );
    } // mapToResponse
    
} // CategoryController
