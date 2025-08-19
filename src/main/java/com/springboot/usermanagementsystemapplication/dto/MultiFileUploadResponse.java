package com.springboot.usermanagementsystemapplication.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MultiFileUploadResponse {
    @Singular
    List<FileResponse> files;
}
