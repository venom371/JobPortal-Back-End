package com.jobportal.user.controller;

import com.jobportal.common.dto.ApiResponse;
import com.jobportal.user.dto.ValidateUser;
import com.jobportal.user.model.User;
import com.jobportal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{emailOrPhoneNo}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String emailOrPhoneNo) {
        User user = userService.getUser(emailOrPhoneNo);

        ApiResponse<User> response = new ApiResponse<>("User found", HttpStatus.OK);
        response.setData(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Void>> validateUser(@RequestBody ValidateUser userObj) {
        userService.validateUser((userObj));
        ApiResponse<Void> response = new ApiResponse<>("", HttpStatus.ACCEPTED);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(response);
    }
}