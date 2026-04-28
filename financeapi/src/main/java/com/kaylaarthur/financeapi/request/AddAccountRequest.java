package com.kaylaarthur.financeapi.request;

import com.kaylaarthur.financeapi.enums.Type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class AddAccountRequest {
    
    @NotBlank
    @Size(max=20)
    private String name;
    @NotNull
    private Type type;
    @NotNull
    private BigDecimal balance;
    
    public AddAccountRequest() {} // AddAccountRequest

    public String getName() { return name; } // getName

    public void setName(String name) { this.name = name; } // setName

    public Type getType() { return type; } // getType

    public void setType(Type type) { this.type = type; } // setType

    public BigDecimal getBalance() { return balance; } // getBalance

    public void setBalance(BigDecimal balance) { this.balance = balance; } // setBalance

} // AddAccountRequest
