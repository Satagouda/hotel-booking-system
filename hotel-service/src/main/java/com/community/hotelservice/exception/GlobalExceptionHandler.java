package com.community.hotelservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation() {
        return ResponseEntity.badRequest().body("Validation failed");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle() {
        return ResponseEntity.status(500).body("Error occurred");
    }
}
