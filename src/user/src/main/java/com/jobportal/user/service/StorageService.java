package com.jobportal.user.service;

import com.jobportal.user.exception.UserException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface StorageService {
    void saveImages(List<MultipartFile> images, String userId);
//    void deleteImage(String imageId);

    default void validateImages(List<MultipartFile> images){
        for(MultipartFile image: images){
            if (image == null || image.isEmpty()) {
                throw new UserException.ImagesNotSent();
            }

            // Check content type
            String contentType = image.getContentType();
            if (contentType == null || !image.getContentType().startsWith("image/")) {
                throw new UserException.InvalidImageFile();
            }
        }
    }

    default String getFileName(MultipartFile image){
        if (image == null || image.getOriginalFilename() == null) {
            return UUID.randomUUID().toString(); // Or throw an exception
        }

        // 1. Sanitize the name
        String cleanName = image.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");

        // 2. Handle empty/blank names after sanitization
        if (cleanName.isBlank()) {
            return UUID.randomUUID().toString();
        }

        // 3. Prepend UUID to ensure uniqueness
        return UUID.randomUUID().toString() + "_" + cleanName;
    }
}
