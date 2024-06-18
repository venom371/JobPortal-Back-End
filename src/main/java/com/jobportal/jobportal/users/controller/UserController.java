package com.jobportal.jobportal.users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal.users.model.User;
import com.jobportal.jobportal.users.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/addUser")
    public void addUser(@RequestBody User userData) {
        userService.addUser(userData);
    }

    @GetMapping("/")
    public String getMethodName() {
        return new String("hello world rr");
    }

    
}
