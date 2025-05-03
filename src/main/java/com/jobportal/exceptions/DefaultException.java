package com.jobportal.exceptions;

import org.springframework.http.HttpStatus;

public class DefaultException extends Exception {

    HttpStatus statusCode;

    public DefaultException(String message, HttpStatus statusCode){
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getHttpStatus(){
        return this.statusCode;
    }
}
