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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        AuthRequest request = new AuthRequest("testuser", "password");
        AuthResponse response = new AuthResponse("jwt.token.here");

        when(authenticateUserUseCase.execute(any(AuthRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt.token.here"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        AuthRequest request = new AuthRequest("nonexistent", "password");

        when(authenticateUserUseCase.execute(any(AuthRequest.class)))
            .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        AuthRequest request = new AuthRequest("", "");

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenUsernameIsMissing() throws Exception {
        String requestJson = "{\"password\":\"password\"}";

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPasswordIsMissing() throws Exception {
        String requestJson = "{\"username\":\"testuser\"}";

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isBadRequest());
    }
}

