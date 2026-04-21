package com.kaylaarthur.financeapi.utility;

import com.kaylaarthur.financeapi.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtility {
    
    public User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
} // SecurityUtility
