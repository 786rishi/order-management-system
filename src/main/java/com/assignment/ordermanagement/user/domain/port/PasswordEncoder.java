package com.assignment.ordermanagement.user.domain.port;

/**
 * Port for password encoding (adapter will use Spring Security's encoder)
 */
public interface PasswordEncoder {

    boolean matches(String rawPassword, String encodedPassword);
}

