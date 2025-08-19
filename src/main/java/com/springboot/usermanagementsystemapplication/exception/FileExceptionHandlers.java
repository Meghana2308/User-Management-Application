package com.springboot.usermanagementsystemapplication.exception;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@Slf4j
@RestControllerAdvice
public class FileExceptionHandlers {

    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<ApiError> handleValidation(FileValidationException ex) {
        log.warn("File validation error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(StoredFileNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(StoredFileNotFoundException ex) {
        log.warn("File not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiError> handleStorage(FileStorageException ex) {
        log.error("File storage error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Could not process file"));
    }

    // Simple error body
    @Value
    static class ApiError {
        int status;
        String error;
        String message;
        OffsetDateTime timestamp;

        static ApiError of(HttpStatus status, String message) {
            return new ApiError(status.value(), status.getReasonPhrase(), message, OffsetDateTime.now());
        }
    }
}