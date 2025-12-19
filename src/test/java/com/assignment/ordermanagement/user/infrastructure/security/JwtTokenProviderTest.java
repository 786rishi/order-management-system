package com.assignment.ordermanagement.user.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private String secret = "mySecretKeyForJWTTokenGenerationAndValidationThatNeedsToBeAtLeast256BitsLong12345678";
    private long expirationTime = 3600000;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secret, expirationTime);
    }

    @Test
    void shouldGenerateToken() {
        String username = "testuser";
        String role = "USER";

        String token = jwtTokenProvider.generateToken(username, role);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String username = "testuser";
        String role = "USER";
        String token = jwtTokenProvider.generateToken(username, role);

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void shouldExtractRoleFromToken() {
        String username = "testuser";
        String role = "PREMIUM_USER";
        String token = jwtTokenProvider.generateToken(username, role);

        String extractedRole = jwtTokenProvider.getRoleFromToken(token);

        assertThat(extractedRole).isEqualTo(role);
    }

    @Test
    void shouldValidateToken() {
        String username = "testuser";
        String role = "USER";
        String token = jwtTokenProvider.generateToken(username, role);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldGenerateTokenForAdmin() {
        String username = "admin";
        String role = "ADMIN";

        String token = jwtTokenProvider.generateToken(username, role);
        String extractedRole = jwtTokenProvider.getRoleFromToken(token);

        assertThat(extractedRole).isEqualTo("ADMIN");
    }

    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {
        String token1 = jwtTokenProvider.generateToken("user1", "USER");
        String token2 = jwtTokenProvider.generateToken("user2", "USER");

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void shouldHandleLongUsernames() {
        String longUsername = "a".repeat(100);
        String token = jwtTokenProvider.generateToken(longUsername, "USER");

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        assertThat(extractedUsername).isEqualTo(longUsername);
    }
}

