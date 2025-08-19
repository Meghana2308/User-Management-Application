package com.springboot.usermanagementsystemapplication.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FileResponse {
    String fileName;
    String contentType;
    long size;
    String downloadUrl; // absolute URL
}