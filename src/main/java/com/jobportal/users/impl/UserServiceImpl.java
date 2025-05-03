package com.jobportal.users.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jobportal.common.JwtUtils;
import com.jobportal.exceptions.DefaultException;
import com.jobportal.exceptions.DuplicateFieldException;
import com.jobportal.users.beans.UserLogin;
import com.jobportal.users.model.User;
import com.jobportal.users.repository.UserRepository;
import com.jobportal.users.service.UserService;

import jakarta.servlet.http.Cookie;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${file.upload-dir}")
    private String uploadImagesDir;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    @Override
    public void createUser(User userData) {
        try {
            userData.setPasswordHash(passwordEncoder.encode(userData.getPassword()));
            userRepository.insert(userData);
        } catch (DuplicateKeyException e) {
            String errorMessage = getDuplicateKeyErrorMessage(e);
            throw new DuplicateFieldException(errorMessage);
        }
    }

    @Override
    public void uploadFiles(List<MultipartFile> files, String userId) throws IOException {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
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

    @Override
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

    @Override
    public User loadUserByUserId(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Optional<User> getUserByEmailOrPhoneNumber(String username) throws DefaultException {
        Optional<User> user;

        try {
            user = userRepository.findByEmail(username);
            if (user.get() == null) {
                user = userRepository.findByPhoneNumber(username);

                if (user.get() == null) {
                    throw new UsernameNotFoundException("Invalid user");
                }
            }
        } catch (Exception e) {
            throw new DefaultException("Invalid user", HttpStatus.NOT_FOUND);
        }

        return user;
    }

    @Override
    public Cookie loginUser(UserLogin payload) throws DefaultException {
        try {
            Optional<User> user = getUserByEmailOrPhoneNumber(payload.getEmailOrPhoneNumber());
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(payload.getEmailOrPhoneNumber(), payload.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtils.generateJwtToken(user.get().getId());
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Use `true` in production (HTTPS only)
            cookie.setPath("/");
            cookie.setMaxAge(jwtExpirationMs / 1000); // 7 days (adjust as needed)
            
            return cookie;
        } catch (BadCredentialsException e) {
            throw new DefaultException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    
}
