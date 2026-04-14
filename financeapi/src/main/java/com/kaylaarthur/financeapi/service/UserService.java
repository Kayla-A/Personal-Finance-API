package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.repository.UserRepo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    } // UserRepo
    
    public User createUser(String name, String email, String password) {

        // check email doesn't already exist
        userRepo.findByEmail(email).ifPresent(u -> {throw new IllegalArgumentException("Email already exists");});
        
        String hashedPass = encoder.encode(password); // do checks for if null
        // save with UserRepo
        User user = new User(name, email, hashedPass);
        return userRepo.save(user);
    } // createUser   

} // UserService
