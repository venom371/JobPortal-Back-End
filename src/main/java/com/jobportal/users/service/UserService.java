package com.jobportal.users.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jobportal.exceptions.DefaultException;
import com.jobportal.users.beans.UserLogin;
import com.jobportal.users.model.User;

import jakarta.servlet.http.Cookie;


public interface UserService {

    void createUser(User userData);

    void uploadFiles(List<MultipartFile> files, String userId) throws IOException;

    void verifyUserEmailAndPhoneNo(String email, String phoneNo);

    User loadUserByUserId(String userId);

    Cookie loginUser(UserLogin payload) throws DefaultException;
}   