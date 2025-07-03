package com.dmtool.dashboard.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/api/files")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    // Inject the directory path from the properties file
    @Value("${upload.dir}")
    private String uploadDir;

    // Endpoint to upload the .md file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            logger.error("No file uploaded.");
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        // Ensure the file is a Markdown file
        if (!file.getOriginalFilename().endsWith(".md")) {
            logger.error("Invalid file type. Only .md files are allowed.");
            return ResponseEntity.badRequest().body("Invalid file type. Only .md files are allowed");
        }

        try {
            // Create the upload directory if it doesn't exist
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path); // Create the directory if not present
            }

            // Save the file
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = path.resolve(fileName);
            file.transferTo(filePath.toFile());

            logger.info("File uploaded successfully: {}", fileName);
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            logger.error("File upload failed: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
        }
    }

    // Endpoint to list all .md files in the upload directory
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<String>> listFiles() {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Ensure the directory exists
            }

            String[] fileNames = directory.list((dir, name) -> name.endsWith(".md"));
            return ResponseEntity.ok(Arrays.asList(fileNames));
        } catch (Exception e) {
            logger.error("Failed to list files: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint to get a specific .md file content
    @GetMapping("/view/{filename}")
    @ResponseBody
    public ResponseEntity<String> viewFile(@PathVariable String filename) {
        try {
            Path path = Paths.get(uploadDir).resolve(filename);
            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }

            String content = new String(Files.readAllBytes(path));
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            logger.error("Failed to read file: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to read file: " + e.getMessage());
        }
    }
}
