package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.model.Category;
import com.kaylaarthur.financeapi.repository.CategoryRepo;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    
    private CategoryRepo catergoryRepo;

    public CategoryService(CategoryRepo catergoryRepo) {
        this.catergoryRepo = catergoryRepo;
    } // CategoryService

    public Category addCategory(long userId, String name) {
        catergoryRepo.findByUserIdAndName(userId, name).ifPresent(u -> {throw new IllegalArgumentException("Category already exists");});
        return catergoryRepo.save(new Category(userId, name));
    } // addCategory

    public Category updateCategory(long categoryId, long userId, String categoryName) {
        Category category = new Category(categoryId, userId);

        if(categoryName != null) {
            catergoryRepo.findByUserIdAndName(userId, categoryName).ifPresent(u -> {throw new IllegalArgumentException("Category already exists");});
            category.setCategoryName(categoryName);
        } // if

        return catergoryRepo.update(category);
    } // updateCategory

    public void deleteCategory(long categoryId, long userId) {
        catergoryRepo.findByCategoryIdAndUserId(categoryId, userId);
        catergoryRepo.delete(categoryId, userId);
    } // deleteCategory

    public Category getCategory(long categoryId, long userId) {
        return catergoryRepo.findByCategoryIdAndUserId(categoryId, userId).orElseThrow(() -> new RuntimeException("Account not found"));
    } // getCategory

    public List<Category> getAllCategories(long userId) {
        return catergoryRepo.findByUserId(userId);
    } // getAllCategories

} // CategoryService
