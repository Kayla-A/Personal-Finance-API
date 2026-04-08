package com.kaylaarthur.financeapi.controller;

import com.kaylaarthur.financeapi.request.CreateUserRequest;
import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.service.UserService;
import com.kaylaarthur.financeapi.response.UserResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Main entry point for users.
 * Contains endpoints for use cases involving the user of the system
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    } // UserController

    /*
        A user can create an account on the system with a name, email and password
        input: name, email, password
        what should happen: validate input, ensue email is unique, hash password,save user
        output: success response + DTO (never retun password it's a security risk )
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {        
        User user = userService.createUser(request.getName(), request.getEmail(), request.getPassword());
        UserResponse response = new UserResponse(user.getId(), user.getName(), user.getPassword());
        return ResponseEntity.status(201).body(response);
    } // createUser
    
} // UserController
