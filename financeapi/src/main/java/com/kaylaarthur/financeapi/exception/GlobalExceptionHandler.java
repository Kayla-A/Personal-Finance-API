package com.kaylaarthur.financeapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

/**
 * 
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    } // handleGeneralException 
    
    /**
     * 
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } // handleBadReequest

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    } // handleBadReequest


} // GlobalExceptionHandler
