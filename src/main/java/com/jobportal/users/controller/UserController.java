package com.jobportal.users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jobportal.common.ApiResponse;
import com.jobportal.users.model.User;
import com.jobportal.users.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody User userData) {
        String userId = UUID.randomUUID().toString();
        userData.setId(userId);
        userService.createUser(userData);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @PostMapping("/uploadImages")
    public ResponseEntity<ApiResponse> uploadFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam("userId") String userId) {
        try{
            userService.uploadFiles(files, userId);
            return ResponseEntity.ok().body(new ApiResponse(true, "Files saved successfully"));
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
        catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage(), e.getStackTrace()));
        }
    }

    @GetMapping("/verifyUserEmailAndPhoneNo")
    public ResponseEntity<String> verifyUserEmailAndPhoneNo(@RequestParam String email, @RequestParam String phoneNo) {
        try{
            userService.verifyUserEmailAndPhoneNo(email, phoneNo);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping("/")
    public String getMethodName() {
        return new String("hello world rr");
    }
}
