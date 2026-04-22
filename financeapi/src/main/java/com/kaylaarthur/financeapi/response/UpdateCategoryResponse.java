package com.kaylaarthur.financeapi.response;

public class UpdateCategoryResponse {
    
    private long categoryId;
    private long userId;
    private String categoryName;
    
    

    public UpdateCategoryResponse(long categoryId, long userId, String categoryName) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.categoryName = categoryName;
    } // UpdateCategoryResponse

    public long getCategoryId() { return categoryId; } // getCategoryId
   
    public long getUserId() { return userId; } // getUserId
    
    public String getCategoryName() { return categoryName; } // getCategoryName

} // UpdateCategoryResponse
