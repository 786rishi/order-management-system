package com.assignment.ordermanagement.user.application.usecase;

import com.assignment.ordermanagement.user.application.dto.AuthRequest;
import com.assignment.ordermanagement.user.application.dto.AuthResponse;
import com.assignment.ordermanagement.user.domain.exception.UserAuthenticationException;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.PasswordEncoder;
import com.assignment.ordermanagement.user.domain.port.UserService;
import com.assignment.ordermanagement.user.infrastructure.security.JwtTokenProvider;

/**
 * Use Case: Authenticate user and generate JWT token
 */
public class AuthenticateUserUseCase {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticateUserUseCase(UserService userService,
                                  PasswordEncoder passwordEncoder,
                                  JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse execute(AuthRequest request) {
        User user = userService.getUserByUsername(request.username());
        
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserAuthenticationException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(token);
    }
}

