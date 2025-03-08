package com.jobportal.users.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobportal.exceptions.DuplicateFieldException;
import com.jobportal.users.model.User;
import com.jobportal.users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadImagesDir;

    public void createUser(User userData) {
        try {
            userRepository.insert(userData);
        } catch (DuplicateKeyException e) {
            String errorMessage = getDuplicateKeyErrorMessage(e);
            throw new DuplicateFieldException(errorMessage);
        }
    }

    public void uploadFiles(List<MultipartFile> files, String userId) throws IOException {
        
        Path rootLocation = Paths.get(uploadImagesDir, userId);
        Files.createDirectories(rootLocation);

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new RuntimeException("Empty file");
            }
            if (!file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Invalid image type");
            }

            String filename = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");

            Path destination = rootLocation.resolve(filename);
            Files.copy(file.getInputStream(), destination);
        }
        
    }

    public void verifyUserEmailAndPhoneNo(String email, String phoneNo) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeErrorException(null, "Email already exist");
        } else if (userRepository.existsByPhoneNumber(phoneNo)) {
            throw new RuntimeErrorException(null, "Phone Number already exist");
        }
    }

    private String getDuplicateKeyErrorMessage(DuplicateKeyException e) {
        String message = e.getMessage();
        if (message != null && message.contains("dup key")) {
            int startIndex = message.indexOf("dup key");
            int endIndex = message.indexOf("}");
            if (startIndex != -1 && endIndex != -1) {
                return "Duplicate key error: " + message.substring(startIndex + 9, endIndex + 1) + " is already used";
            }
        }
        return "Duplicate key error";
    }
}
