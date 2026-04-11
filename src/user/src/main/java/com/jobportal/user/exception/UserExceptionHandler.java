package com.jobportal.user.exception;

import com.jobportal.common.dto.ApiResponse;
import com.jobportal.common.exception.CommonExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class UserExceptionHandler {

    private final CommonExceptionHandler commonExceptionHandler;

    @ExceptionHandler(UserException.EmailAlreadyExist.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailExists(UserException.EmailAlreadyExist ex) {
        return commonExceptionHandler.buildError(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(UserException.PhoneNumberAlreadyExist.class)
    public ResponseEntity<ApiResponse<Void>> handlePhoneNumberExists(UserException.PhoneNumberAlreadyExist ex) {
        return commonExceptionHandler.buildError(HttpStatus.CONFLICT, "PHONE_NUMBER_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(UserException.UserDoesNotExist.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotExists(UserException.UserDoesNotExist ex) {
        return commonExceptionHandler.buildError(HttpStatus.NOT_FOUND, "USER_NOT_EXIST", ex.getMessage());
    }

    @ExceptionHandler(UserException.UserCreationFailed.class)
    public ResponseEntity<ApiResponse<Void>> handleUserCreationFailed(UserException.UserCreationFailed ex) {
        log.error("User creation failed: ", ex.getCause());
        return commonExceptionHandler.buildError(HttpStatus.INTERNAL_SERVER_ERROR, "USER_CREATION_FAILED", ex.getMessage());
    }

    @ExceptionHandler(UserException.ImagesNotSent.class)
    public ResponseEntity<ApiResponse<Void>> handleUserImagesNotSent(UserException.ImagesNotSent ex) {
        return commonExceptionHandler.buildError(HttpStatus.EXPECTATION_FAILED, "USER_IMAGES_NOT_SENT", ex.getMessage());
    }

    @ExceptionHandler(UserException.InvalidImageFile.class)
    public ResponseEntity<ApiResponse<Void>> handleUserInvalidImageFile(UserException.InvalidImageFile ex) {
        return commonExceptionHandler.buildError(HttpStatus.BAD_REQUEST, "INVALID_IMAGE_SENT", ex.getMessage());
    }

    @ExceptionHandler(UserException.InvalidUserPassword.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidUserPassword(UserException.InvalidUserPassword ex) {
        log.info("Login attempt failed: Invalid password");
        return commonExceptionHandler.buildError(HttpStatus.UNAUTHORIZED, "INVALID_USER_PASSWORD", ex.getMessage());
    }
}
