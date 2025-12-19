package com.assignment.ordermanagement.product.domain.exception;

/**
 * Domain exception for product not found scenarios
 */
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

