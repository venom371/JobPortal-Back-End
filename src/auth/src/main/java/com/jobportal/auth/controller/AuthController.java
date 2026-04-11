package com.jobportal.auth.controller;

import com.jobportal.auth.dto.LoginRequest;
import com.jobportal.auth.service.AuthService;
import com.jobportal.auth.service.JWTService;
import com.jobportal.common.dto.ApiResponse;
import com.jobportal.user.dto.CreateUserRequest;
import com.jobportal.user.model.User;
import com.jobportal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("service/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> loginUser(@RequestBody LoginRequest loginRequest){
        ResponseCookie cookie = authService.loginUser(loginRequest);
        ApiResponse<Void> response = new ApiResponse<>("User logged in Successfully", HttpStatus.OK);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(){
        ResponseCookie cookie = jwtService.clearJWTCookie();
        ApiResponse<Void> response = new ApiResponse<>("User logout successfully", HttpStatus.OK);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestPart("userData") CreateUserRequest userObj,
                                                    @RequestPart(value = "images") List<MultipartFile> images){
        User user = userService.createUser(userObj, images);
        ResponseCookie cookie = jwtService.createJWTCookie(user.getUserId(), user.getEmailId());
        ApiResponse<Void> response = new ApiResponse<>("User Signed up successfully", HttpStatus.CREATED);

        return ResponseEntity.status(response.getStatus())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}
