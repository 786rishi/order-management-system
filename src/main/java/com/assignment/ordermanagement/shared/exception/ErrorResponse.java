package com.assignment.ordermanagement.shared.exception;

import java.time.Instant;

/**
 * Standard error response format
 */
public record ErrorResponse(
        int status,
        String message,
        Instant timestamp
) {
}

