package com.jobportal.common.exception;

import com.jobportal.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Caused by: {}", ex.getMessage(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ex.getMessage());
    }

    public ResponseEntity<ApiResponse<Void>> buildError(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .status(status)
                        .error(error)
                        .message(message)
                        .build());
    }
}
