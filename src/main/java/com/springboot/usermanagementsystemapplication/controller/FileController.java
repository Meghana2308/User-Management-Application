package com.springboot.usermanagementsystemapplication.controller;

import com.springboot.usermanagementsystemapplication.config.FileStorageConfig;
import com.springboot.usermanagementsystemapplication.dto.FileResponse;
import com.springboot.usermanagementsystemapplication.dto.MultiFileUploadResponse;
import com.springboot.usermanagementsystemapplication.exception.StoredFileNotFoundException;
import com.springboot.usermanagementsystemapplication.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService storage;
    private final FileStorageConfig cfg;

    /* ========= Single Upload ========= */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> upload(@RequestPart("file") MultipartFile file) {
        String stored = storage.store(file);
        return ResponseEntity.ok(buildResponse(stored, file.getContentType(), file.getSize()));
    }

    /* ========= Multiple Upload ========= */
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiFileUploadResponse> uploadMultiple(@RequestPart("files") List<MultipartFile> files) {
        var storedNames = storage.storeAll(files);
        var builder = MultiFileUploadResponse.builder();

        for (int i = 0; i < storedNames.size(); i++) {
            MultipartFile f = files.get(i);
            builder.file(FileResponse.builder()
                    .fileName(storedNames.get(i))
                    .contentType(f.getContentType())
                    .size(f.getSize())
                    .downloadUrl(downloadUrl(storedNames.get(i)))
                    .build());
        }
        return ResponseEntity.ok(builder.build());
    }

    /* ========= Download ========= */
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName) {
        Resource resource = storage.loadAsResource(fileName);

        // Try to resolve content-type; fallback to octet-stream
        String contentType = "application/octet-stream";
        try {
            Path p = Path.of(resource.getFile().getAbsolutePath());
            String detected = Files.probeContentType(p);
            if (detected != null) contentType = detected;
        } catch (IOException ex) {
            log.debug("Could not determine content-type for {}. Using default.", fileName);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(fileName).build().toString())
                .body(resource);
    }

    /* ========= Optional: Delete ========= */
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> delete(@PathVariable String fileName) {
        boolean deleted = storage.delete(fileName);
        if (!deleted) throw new StoredFileNotFoundException("File not found: " + fileName);
        return ResponseEntity.noContent().build();
    }

    /* ===== Helpers ===== */
    private FileResponse buildResponse(String storedName, String contentType, long size) {
        return FileResponse.builder()
                .fileName(storedName)
                .contentType(contentType)
                .size(size)
                .downloadUrl(downloadUrl(storedName))
                .build();
    }

    private String downloadUrl(String storedName) {
        // Prefer current request context; falls back to configured base URL
        String base = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        if (base == null || base.isBlank()) base = cfg.getBaseUrl();
        return base + "/api/files/" + storedName;
    }
}