package com.assignment.ordermanagement.user.domain.exception;

/**
 * Domain exception for user not found scenarios
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

