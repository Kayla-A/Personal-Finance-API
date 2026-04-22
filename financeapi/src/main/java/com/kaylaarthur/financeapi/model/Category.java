package com.kaylaarthur.financeapi.model;

public class Category {
    
    private long categoryId;
    private long userId;
    private String categoryName;
    
    public Category(long categoryId, long userId, String categoryName) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.categoryName = categoryName;
    } // Category

    public Category(long userId, String categoryName) {
        this.userId = userId;
        this.categoryName = categoryName;
    } // Category


    public Category(long categoryId, long userId) {
        this.categoryId = categoryId;
        this.userId = userId;
    } // Category

    public long getCategoryId() { return categoryId; } // getCategoryId

    public void setCategoryId(long categoryId) { this.categoryId = categoryId; } // setCategoryId

    public long getUserId() { return userId; } // getUserId

    public void setUserId(long userId) { this.userId = userId; } // setUserId

    public String getCategoryName() { return categoryName; } // getCategoryName

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; } // setCategoryName

} // Category
