package com.jobportal.common.dto;

import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private HttpStatus status;
    private Object error;

    public ApiResponse(String message, HttpStatus status){
        this.message = message;
        this.status = status;
        this.success = true;
    }
    public ApiResponse(String message, HttpStatus status, boolean success){
        this.message = message;
        this.status = status;
        this.success = success;
    }
}
