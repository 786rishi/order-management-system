package com.assignment.ordermanagement.user.domain.exception;

/**
 * Domain exception for authentication failures
 */
public class UserAuthenticationException extends RuntimeException {

    public UserAuthenticationException(String message) {
        super(message);
    }

    public UserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

