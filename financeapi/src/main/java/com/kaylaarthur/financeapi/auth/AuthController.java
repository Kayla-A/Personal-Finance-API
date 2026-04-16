package com.kaylaarthur.financeapi.auth;

import com.kaylaarthur.financeapi.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kaylaarthur.financeapi.request.LoginRequest;
import com.kaylaarthur.financeapi.response.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    } // AuthController

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        User user = authService.loginUser(request.getEmail(), request.getPassword());
        LoginResponse response = new LoginResponse(jwtService.generateToken(user), user.getId(), user.getName(), user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } // loginUser
    
} // AuthController
