package com.assignment.ordermanagement.user.adapter.rest;

import com.assignment.ordermanagement.user.application.dto.AuthRequest;
import com.assignment.ordermanagement.user.application.dto.AuthResponse;
import com.assignment.ordermanagement.user.application.usecase.AuthenticateUserUseCase;
import com.assignment.ordermanagement.user.domain.exception.UserAuthenticationException;
import com.assignment.ordermanagement.user.domain.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationThatNeedsToBeAtLeast256BitsLong12345678",
    "jwt.expiration=3600000"
})
class AuthControllerTest {

    private static final String API_AUTH_LOGIN_URL = "/api/auth/login";
    private static final String TEST_USER = "testuser";
    private static final String NON_EXISTENT_USER = "nonexistent";
    private static final String PASSWORD = "password";
    private static final String JWT_TOKEN = "jwt.token.here";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String JSON_PASSWORD_ONLY = "{\"password\":\"password\"}";
    private static final String JSON_USERNAME_ONLY = "{\"username\":\"testuser\"}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest(TEST_USER, PASSWORD);
        AuthResponse response = new AuthResponse(JWT_TOKEN);

        when(authenticateUserUseCase.execute(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post(API_AUTH_LOGIN_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value(JWT_TOKEN));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        AuthRequest request = new AuthRequest(NON_EXISTENT_USER, PASSWORD);

        when(authenticateUserUseCase.execute(any(AuthRequest.class)))
            .thenThrow(new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        mockMvc.perform(post(API_AUTH_LOGIN_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        AuthRequest request = new AuthRequest("", "");

        mockMvc.perform(post(API_AUTH_LOGIN_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenUsernameIsMissing() throws Exception {
        mockMvc.perform(post(API_AUTH_LOGIN_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_PASSWORD_ONLY))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPasswordIsMissing() throws Exception {
        mockMvc.perform(post(API_AUTH_LOGIN_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_USERNAME_ONLY))
            .andExpect(status().isBadRequest());
    }
}

