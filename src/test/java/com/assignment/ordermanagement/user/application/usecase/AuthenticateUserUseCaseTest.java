package com.assignment.ordermanagement.user.application.usecase;

import com.assignment.ordermanagement.user.application.dto.AuthRequest;
import com.assignment.ordermanagement.user.application.dto.AuthResponse;
import com.assignment.ordermanagement.user.domain.exception.UserAuthenticationException;
import com.assignment.ordermanagement.user.domain.model.Role;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.PasswordEncoder;
import com.assignment.ordermanagement.user.domain.port.UserService;
import com.assignment.ordermanagement.user.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthenticateUserUseCase authenticateUserUseCase;

    @BeforeEach
    void setUp() {
        authenticateUserUseCase = new AuthenticateUserUseCase(userService, passwordEncoder, jwtTokenProvider);
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        String username = "testuser";
        String password = "password";
        String encodedPassword = "$2a$10$encodedPassword";
        String token = "jwt.token.here";
        
        AuthRequest request = new AuthRequest(username, password);
        User user = new User(1L, username, encodedPassword, Role.USER);

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.generateToken(username, "USER")).thenReturn(token);

        AuthResponse response = authenticateUserUseCase.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(token);
        verify(userService).getUserByUsername(username);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(jwtTokenProvider).generateToken(username, "USER");
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        String username = "testuser";
        String password = "wrongpassword";
        String encodedPassword = "$2a$10$encodedPassword";
        
        AuthRequest request = new AuthRequest(username, password);
        User user = new User(1L, username, encodedPassword, Role.USER);

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThatThrownBy(() -> authenticateUserUseCase.execute(request))
            .isInstanceOf(UserAuthenticationException.class)
            .hasMessage("Invalid credentials");
    }

    @Test
    void shouldAuthenticatePremiumUser() {
        String username = "premiumuser";
        String password = "password";
        String encodedPassword = "$2a$10$encodedPassword";
        String token = "jwt.token.here";
        
        AuthRequest request = new AuthRequest(username, password);
        User user = new User(1L, username, encodedPassword, Role.PREMIUM_USER);

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.generateToken(username, "PREMIUM_USER")).thenReturn(token);

        AuthResponse response = authenticateUserUseCase.execute(request);

        assertThat(response).isNotNull();
        verify(jwtTokenProvider).generateToken(username, "PREMIUM_USER");
    }

    @Test
    void shouldAuthenticateAdmin() {
        String username = "admin";
        String password = "password";
        String encodedPassword = "$2a$10$encodedPassword";
        String token = "jwt.token.here";
        
        AuthRequest request = new AuthRequest(username, password);
        User user = new User(1L, username, encodedPassword, Role.ADMIN);

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.generateToken(username, "ADMIN")).thenReturn(token);

        AuthResponse response = authenticateUserUseCase.execute(request);

        assertThat(response).isNotNull();
        verify(jwtTokenProvider).generateToken(username, "ADMIN");
    }
}

