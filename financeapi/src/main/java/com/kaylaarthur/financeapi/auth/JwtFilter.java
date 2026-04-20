package com.kaylaarthur.financeapi.auth;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterChain;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter{
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    } // JwtFilter

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        } // if

        String token = authHeader.substring(7);
        if(!jwtService.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } // if
        
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(null, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);


    } // doFilterInternal 
} // JwtFilter
