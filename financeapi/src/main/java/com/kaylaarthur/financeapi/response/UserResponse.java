package com.kaylaarthur.financeapi.response;

public class UserResponse {
    private long id;
    private String name;
    private String email;

    public UserResponse(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    } // UserResponse

    public long getId() { return id; } // getId

    public String getName() { return name; } // getName

    public String getEmail() { return email; } // getEmail
} // UserResponse
