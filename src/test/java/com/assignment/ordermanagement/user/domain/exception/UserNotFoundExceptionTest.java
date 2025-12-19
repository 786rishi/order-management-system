package com.assignment.ordermanagement.user.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "User not found";
        
        UserNotFoundException exception = new UserNotFoundException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldBeRuntimeException() {
        UserNotFoundException exception = new UserNotFoundException("Test");
        
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

