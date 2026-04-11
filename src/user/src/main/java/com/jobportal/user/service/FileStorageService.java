package com.jobportal.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@ConditionalOnProperty(
        name  = "storage.type",
        havingValue = "local",              // ← active when storage.type=local
        matchIfMissing = true
)
public class FileStorageService implements StorageService {
    @Value("${file.upload-dir}")
    private String uploadImagesDir;

    @Override
    public void saveImages(List<MultipartFile> images, String userId) {
        validateImages(images);
        Path rootLocation = Paths.get(uploadImagesDir, userId);
        try {
            Files.createDirectories(rootLocation);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        images.forEach((image) -> {
            String filename = getFileName(image);
            Path destination = rootLocation.resolve(filename);
            try {
                Files.copy(image.getInputStream(), destination);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
