package com.jobportal.jobportal.users.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.jobportal.users.model.User;
import com.jobportal.jobportal.users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addUser(User userData){
        
        userRepository.insert(userData);
    }
}
