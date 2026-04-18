package com.example.demowebshop.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            return "/uploads/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi upload file");
        }
    }

    public void deleteFile(String filePath) {
        try {
            if (filePath == null) return;

            String realPath = filePath.replace("/uploads/", "uploads/");
            Path path = Paths.get(realPath);
            Files.deleteIfExists(path);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}