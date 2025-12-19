package com.assignment.ordermanagement.order.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutOfStockExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Product is out of stock";
        
        OutOfStockException exception = new OutOfStockException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "Product is out of stock";
        Throwable cause = new RuntimeException("Root cause");
        
        OutOfStockException exception = new OutOfStockException(message, cause);
        
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void shouldBeRuntimeException() {
        OutOfStockException exception = new OutOfStockException("Test");
        
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

