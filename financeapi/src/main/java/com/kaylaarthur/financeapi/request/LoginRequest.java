package com.kaylaarthur.financeapi.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64)
    private String password;

    public LoginRequest() {} // CreateLoginRequest

    public void setEmail(String email) { this.email = email;} // setEmail

    public String getEmail() { return email; } // getEmail

    public void setPassword(String password) { this.password = password;} // setPassword

    public String getPassword() { return password; } // getPasswrd

} // LoginRequest
