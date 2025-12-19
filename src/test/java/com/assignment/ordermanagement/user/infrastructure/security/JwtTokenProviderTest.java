package com.assignment.ordermanagement.user.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String JWT_SECRET = "mySecretKeyForJWTTokenGenerationAndValidationThatNeedsToBeAtLeast256BitsLong12345678";
    private static final long JWT_EXPIRATION = 3600000L;
    private static final String TEST_USER = "testuser";
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_PREMIUM_USER = "PREMIUM_USER";
    private static final String ADMIN_USER = "admin";
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";
    private static final String INVALID_TOKEN = "invalid.token.here";
    private static final int LONG_USERNAME_LENGTH = 100;

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(JWT_SECRET, JWT_EXPIRATION);
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtTokenProvider.generateToken(TEST_USER, ROLE_USER);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtTokenProvider.generateToken(TEST_USER, ROLE_USER);

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        assertThat(extractedUsername).isEqualTo(TEST_USER);
    }

    @Test
    void shouldExtractRoleFromToken() {
        String token = jwtTokenProvider.generateToken(TEST_USER, ROLE_PREMIUM_USER);

        String extractedRole = jwtTokenProvider.getRoleFromToken(token);

        assertThat(extractedRole).isEqualTo(ROLE_PREMIUM_USER);
    }

    @Test
    void shouldValidateToken() {
        String token = jwtTokenProvider.generateToken(TEST_USER, ROLE_USER);

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        boolean isValid = jwtTokenProvider.validateToken(INVALID_TOKEN);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldGenerateTokenForAdmin() {
        String token = jwtTokenProvider.generateToken(ADMIN_USER, ROLE_ADMIN);
        String extractedRole = jwtTokenProvider.getRoleFromToken(token);

        assertThat(extractedRole).isEqualTo(ROLE_ADMIN);
    }

    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {
        String token1 = jwtTokenProvider.generateToken(USER_1, ROLE_USER);
        String token2 = jwtTokenProvider.generateToken(USER_2, ROLE_USER);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    void shouldHandleLongUsernames() {
        String longUsername = "a".repeat(LONG_USERNAME_LENGTH);
        String token = jwtTokenProvider.generateToken(longUsername, ROLE_USER);

        String extractedUsername = jwtTokenProvider.getUsernameFromToken(token);

        assertThat(extractedUsername).isEqualTo(longUsername);
    }
}

