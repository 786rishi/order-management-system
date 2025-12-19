package com.assignment.ordermanagement.user.config;

import com.assignment.ordermanagement.user.application.usecase.AuthenticateUserUseCase;
import com.assignment.ordermanagement.user.domain.port.PasswordEncoder;
import com.assignment.ordermanagement.user.domain.port.UserRepository;
import com.assignment.ordermanagement.user.domain.port.UserService;
import com.assignment.ordermanagement.user.domain.service.UserDomainService;
import com.assignment.ordermanagement.user.infrastructure.persistence.adapter.UserRepositoryAdapter;
import com.assignment.ordermanagement.user.infrastructure.persistence.mapper.UserEntityMapper;
import com.assignment.ordermanagement.user.infrastructure.persistence.repository.UserRepositoryJpa;
import com.assignment.ordermanagement.user.infrastructure.security.JwtTokenProvider;
import com.assignment.ordermanagement.user.infrastructure.security.PasswordEncoderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for User/Auth feature
 * Wires all layers together following dependency inversion
 */
@Configuration
public class UserConfig {

    // Infrastructure Layer
    @Bean
    public UserEntityMapper userEntityMapper() {
        return new UserEntityMapper();
    }

    @Bean
    public UserRepository userRepository(UserRepositoryJpa jpaRepository, UserEntityMapper mapper) {
        return new UserRepositoryAdapter(jpaRepository, mapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder(org.springframework.security.crypto.password.PasswordEncoder springEncoder) {
        return new PasswordEncoderAdapter(springEncoder);
    }

    // Domain Layer
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserDomainService(userRepository);
    }

    // Application Layer - Use Cases
    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        return new AuthenticateUserUseCase(userService, passwordEncoder, jwtTokenProvider);
    }

}

