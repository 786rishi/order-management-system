package com.assignment.ordermanagement.shared.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Validation error response format
 */
public record ValidationErrorResponse(
        int status,
        String message,
        Map<String, String> errors,
        Instant timestamp
) {
}

