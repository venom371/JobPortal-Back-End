package com.jobportal.jobportal.users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal.users.model.User;
import com.jobportal.jobportal.users.model.UserAvatar;
import com.jobportal.jobportal.users.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody User userData) {
        try{
            userService.addUser(userData);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
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

    @PostMapping("/uploadAvatar")
    public ResponseEntity<String> uploadAvatar(@RequestBody UserAvatar avatar){
        if(userService.uploadAvatar(avatar)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        
    }
    
    

    @GetMapping("/")
    public String getMethodName() {
        return new String("hello world rr");
    }
}
