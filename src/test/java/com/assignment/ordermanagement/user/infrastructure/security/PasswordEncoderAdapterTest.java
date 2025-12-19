package com.assignment.ordermanagement.user.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    @Mock
    private PasswordEncoder springEncoder;

    private PasswordEncoderAdapter passwordEncoderAdapter;

    @BeforeEach
    void setUp() {
        passwordEncoderAdapter = new PasswordEncoderAdapter(springEncoder);
    }

    @Test
    void shouldMatchPassword() {
        String rawPassword = "password";
        String encodedPassword = "$2a$10$encodedPassword";

        when(springEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = passwordEncoderAdapter.matches(rawPassword, encodedPassword);

        assertThat(result).isTrue();
        verify(springEncoder).matches(rawPassword, encodedPassword);
    }

    @Test
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        String rawPassword = "wrongpassword";
        String encodedPassword = "$2a$10$encodedPassword";

        when(springEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean result = passwordEncoderAdapter.matches(rawPassword, encodedPassword);

        assertThat(result).isFalse();
        verify(springEncoder).matches(rawPassword, encodedPassword);
    }
}

