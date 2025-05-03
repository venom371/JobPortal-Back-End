package com.jobportal.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jobportal.common.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKey(DuplicateFieldException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Duplicate Entry",
            ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(JWTException.class)
    public ResponseEntity<ApiResponse> handleJWTException(JWTException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, ex.getMessage()));
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<ApiResponse> handleDefaultException(DefaultException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(new ApiResponse(false, ex.getMessage()));
    }
}

@Data 
@Builder
@AllArgsConstructor
class ErrorResponse {
    private int status;
    private String error;
    private String message;
}