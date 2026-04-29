package com.kaylaarthur.financeapi.request;

import jakarta.validation.constraints.NotBlank;

public class AddCategoryRequest {
    
    @NotBlank
    private String name;

    public AddCategoryRequest() {} // AddCategoryRequest

    public String getName() { return name; } // getName

    public void setName(String name) { this.name = name; } // setName

    
} // AddCategoryRequest
