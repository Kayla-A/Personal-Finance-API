package com.kaylaarthur.financeapi.auth;

import com.kaylaarthur.financeapi.model.User;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JwtService {
  
    private final String jwtSecret;

    public JwtService(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    } // JJwtService

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(getSecretKey()) 
                .compact();
        //         .setSubject(user.getEmail())
        //         .setIssuedAt(new Date())
        //         .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
        //         .signWith(getSecretKey(), SignatureAlgorithm.HS256)
        //         .compact();
    } // generateToken

    private SecretKey getSecretKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }  // getSecretKey

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch(Exception e) {
            return false;
        } // try-catch
    } // isTokenValid

} // JwtService
