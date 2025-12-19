package com.assignment.ordermanagement.user.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserAuthenticationExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Authentication failed";
        
        UserAuthenticationException exception = new UserAuthenticationException(message);
        
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void shouldBeRuntimeException() {
        UserAuthenticationException exception = new UserAuthenticationException("Test");
        
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}

