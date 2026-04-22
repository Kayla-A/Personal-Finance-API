package com.kaylaarthur.financeapi.request;

public class UpdateCategoryRequest {
    private String categoryName;

    public UpdateCategoryRequest(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() { return categoryName; } // getCategoryName

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; } // setCategoryName

} // UpdateCategoryRequest
