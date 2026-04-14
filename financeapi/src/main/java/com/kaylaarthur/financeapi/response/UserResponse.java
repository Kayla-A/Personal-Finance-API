package com.kaylaarthur.financeapi.response;

public class UserResponse {
    
    private final long id;
    private final String name;
    private final String email;

    public UserResponse(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    } // UserResponse

    public long getId() { return id; } // getId

    public String getName() { return name; } // getName

    public String getEmail() { return email; } // getEmail
} // UserResponse
