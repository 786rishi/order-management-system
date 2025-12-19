package com.assignment.ordermanagement.order.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Order not found";
        
        OrderNotFoundException exception = new OrderNotFoundException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldBeRuntimeException() {
        OrderNotFoundException exception = new OrderNotFoundException("Test");
        
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

