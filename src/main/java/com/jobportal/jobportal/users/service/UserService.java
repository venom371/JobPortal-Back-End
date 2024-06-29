package com.jobportal.jobportal.users.service;

import java.io.File;
import java.io.IOException;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jobportal.jobportal.users.model.User;
import com.jobportal.jobportal.users.model.UserAvatar;
import com.jobportal.jobportal.users.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadImagesDir;

    public void addUser(User userData){
        try{
            userRepository.insert(userData);
        }
        catch(RuntimeException e){
            String errorMessage = getDuplicateKeyErrorMessage(e);
            throw new RuntimeException(errorMessage);
        }
    }

    public void verifyUserEmailAndPhoneNo(String email, String phoneNo){
        if(userRepository.existsByEmail(email)){
            throw new RuntimeErrorException(null, "Email already exist");
        }
        else if(userRepository.existsByPhoneNumber(phoneNo)){
            throw new RuntimeErrorException(null, "Phone Number already exist");
        }
    }

    public boolean uploadAvatar(UserAvatar avatar){
        try {
            User userProfile = userRepository.findByEmail(avatar.getEmail());

            String fileFolderPath = uploadImagesDir + userProfile.getFirstName() + "_" + userProfile.getLastName() + "_" + userProfile.getEmail();
            StringBuilder fileName = new StringBuilder("image_");
            
            File directory = new File(fileFolderPath);
            if (!directory.exists()) {
                directory.mkdirs();
                fileName.append(1);
            }
            else {
                fileName.append(directory.listFiles().length);
            }
            
            avatar.getImage().transferTo(new File(fileFolderPath + "/" + fileName.toString()));

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/").path(fileName.toString()).toUriString();
            
            userProfile.setAvatarString(fileDownloadUri);

            userRepository.save(userProfile);
            return true;
        } catch (IllegalStateException | IOException e) {
            return false;
        }

    }

    private String getDuplicateKeyErrorMessage(RuntimeException e) {
        String message = e.getMessage();
        if (message != null && message.contains("dup key")) {
            int startIndex = message.indexOf("dup key");
            int endIndex = message.indexOf("}");
            if (startIndex != -1 && endIndex != -1) {
                return "Duplicate key error: " + message.substring(startIndex, endIndex + 1);
            }
        }
        return "Duplicate key error";
    }
}
