package com.springboot.usermanagementsystemapplication.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Getter
public class FileStorageConfig {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.unique-names:true}")
    private boolean uniqueNames;

    private final Set<String> allowedTypes;

    public FileStorageConfig(@Value("${file.allowed-types}") String allowedCsv) {
        this.allowedTypes = Stream.of(allowedCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
}