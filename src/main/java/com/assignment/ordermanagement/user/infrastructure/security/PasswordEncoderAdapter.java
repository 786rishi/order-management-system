package com.assignment.ordermanagement.user.infrastructure.security;

import com.assignment.ordermanagement.user.domain.port.PasswordEncoder;

/**
 * Adapter for Spring Security's PasswordEncoder
 */
public class PasswordEncoderAdapter implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder springEncoder;

    public PasswordEncoderAdapter(org.springframework.security.crypto.password.PasswordEncoder springEncoder) {
        this.springEncoder = springEncoder;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springEncoder.matches(rawPassword, encodedPassword);
    }
}

