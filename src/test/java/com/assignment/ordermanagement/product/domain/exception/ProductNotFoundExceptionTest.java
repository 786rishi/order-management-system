package com.assignment.ordermanagement.product.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Product not found";
        
        ProductNotFoundException exception = new ProductNotFoundException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Product not found";
        Throwable cause = new RuntimeException("Root cause");
        
        ProductNotFoundException exception = new ProductNotFoundException(message, cause);
        
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void shouldBeRuntimeException() {
        ProductNotFoundException exception = new ProductNotFoundException("Test");
        
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

