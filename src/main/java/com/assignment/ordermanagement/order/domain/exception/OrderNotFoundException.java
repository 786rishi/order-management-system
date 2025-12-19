package com.assignment.ordermanagement.order.domain.exception;

/**
 * Domain exception for order not found scenarios
 */
public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(String message) {
        super(message);
    }
    
    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

