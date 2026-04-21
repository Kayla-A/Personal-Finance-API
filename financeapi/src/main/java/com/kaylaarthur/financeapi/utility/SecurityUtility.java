package com.kaylaarthur.financeapi.utility;

import com.kaylaarthur.financeapi.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

@Component
public class SecurityUtility {
    
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if(auth == null || !(auth.getPrincipal() instanceof User)) {
            throw new RuntimeException("User is not authenticated");
        } // if

        return (User) auth.getPrincipal();
    } // getCurrentUser
} // SecurityUtility
