package com.kaylaarthur.financeapi.response;

public class LoginResponse {
    // return token and user
    private final String token;
    private final long id;
    private final String name;
    private final String email;

    public LoginResponse(String token, long id, String name, String email) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
    } // LoginResponse

    public String getToken() { return token; } // getToken
   
    public long getId() { return id; } // getId

    public String getName() { return name; } // getName

    public String getEmail() { return email; } // getEmail

} // LoginResponse 
