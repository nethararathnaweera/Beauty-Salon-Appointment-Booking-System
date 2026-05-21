package com.miks.staffmanagement.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> validationErrors
) {
    public static ApiError of(HttpStatus status, String message) {
        return new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, null);
    }

    public static ApiError ofValidation(HttpStatus status, String message, Map<String, String> validationErrors) {
        return new ApiError(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, validationErrors);
    }
}

