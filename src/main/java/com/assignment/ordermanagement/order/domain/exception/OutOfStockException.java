package com.assignment.ordermanagement.order.domain.exception;

/**
 * Domain exception for out of stock scenarios
 */
public class OutOfStockException extends RuntimeException {
    
    public OutOfStockException(String message) {
        super(message);
    }
    
    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}

