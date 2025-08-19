package com.springboot.usermanagementsystemapplication.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    String store(MultipartFile file);                 // returns stored filename
    List<String> storeAll(List<MultipartFile> files); // DRY multi-upload
    Resource loadAsResource(String storedFileName);
    boolean delete(String storedFileName);
}