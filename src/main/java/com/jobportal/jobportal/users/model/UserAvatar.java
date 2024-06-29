package com.jobportal.jobportal.users.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserAvatar {
    private String email;
    private MultipartFile image;
}
