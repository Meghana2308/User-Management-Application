package com.springboot.usermanagementsystemapplication.service.impl;

import com.springboot.usermanagementsystemapplication.config.FileStorageConfig;
import com.springboot.usermanagementsystemapplication.exception.FileStorageException;
import com.springboot.usermanagementsystemapplication.exception.FileValidationException;
import com.springboot.usermanagementsystemapplication.exception.StoredFileNotFoundException;
import com.springboot.usermanagementsystemapplication.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {

    private final FileStorageConfig cfg;

    private Path root() {
        try {
            Path root = Paths.get(cfg.getUploadDir()).toAbsolutePath().normalize();
            Files.createDirectories(root);
            return root;
        } catch (IOException e) {
            throw new FileStorageException("Unable to initialize storage directory", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        validate(file);
        String sanitized = sanitize(Objects.requireNonNull(file.getOriginalFilename()));
        String targetName = cfg.isUniqueNames() ? uniquify(sanitized) : sanitized;

        Path target = root().resolve(targetName);
        try {
            // copy stream safely; replace if same name and uniqueNames=false
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored file: {}", target);
            return targetName;
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + sanitized, e);
        }
    }

    @Override
    public List<String> storeAll(List<MultipartFile> files) {
        // Fail-fast on empty list
        if (files == null || files.isEmpty()) {
            throw new FileValidationException("No files provided");
        }
        return files.stream()
                .map(this::store) // DRY: reuse single-store validation + logic
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Resource loadAsResource(String storedFileName) {
        try {
            Path file = root().resolve(storedFileName).normalize();
            if (!Files.exists(file) || !Files.isReadable(file)) {
                throw new StoredFileNotFoundException("File not found: " + storedFileName);
            }
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new StoredFileNotFoundException("File not readable: " + storedFileName);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new StoredFileNotFoundException("Invalid file path: " + storedFileName, e);
        }
    }

    @Override
    public boolean delete(String storedFileName) {
        try {
            Path file = root().resolve(storedFileName).normalize();
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file: " + storedFileName, e);
        }
    }

    /* ===== Helpers ===== */

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new FileValidationException("Empty file is not allowed");
        String ct = StringUtils.defaultString(file.getContentType(), "");
        if (!cfg.getAllowedTypes().contains(ct)) {
            throw new FileValidationException("Unsupported content-type: " + ct);
        }
    }

    // Prevent path traversal
    private String sanitize(String name) {
        String cleaned = name.replace("\\", "/");
        cleaned = cleaned.substring(cleaned.lastIndexOf('/') + 1);
        if (cleaned.contains("..")) throw new FileValidationException("Invalid file name");
        return cleaned;
    }

    private String uniquify(String baseName) {
        String name = baseName;
        String ext = "";
        int dot = baseName.lastIndexOf('.');
        if (dot > 0) {
            name = baseName.substring(0, dot);
            ext = baseName.substring(dot); // includes dot
        }
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        String rnd = UUID.randomUUID().toString().substring(0, 8);
        return name + "_" + stamp + "_" + rnd + ext;
    }
}