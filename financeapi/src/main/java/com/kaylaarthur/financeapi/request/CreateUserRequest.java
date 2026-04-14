package com.kaylaarthur.financeapi.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 32)
    @Pattern(
        regexp = "^\\w[\\w\\-''.]{2,31}$",
        message = "Name must start with a word character"
    )
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,64}$",
        message = "Password must contain upper, lower, number and special character"
    )
    private String password;

    public CreateUserRequest() {} // CreateUserRequest

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

} // CreateUserRequest
