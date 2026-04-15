package com.kaylaarthur.financeapi.auth;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.repository.UserRepo;

@Service
public class AuthService {
    
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;
     
    public AuthService(UserRepo userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    } // AuthService

    public User loginUser(String email, String password) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        if(!encoder.matches(password, user.getHashedPassword())) { throw new IllegalArgumentException("Password does not match email"); }
        return user;
    } // loginUser
} // AuthService
