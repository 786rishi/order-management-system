package com.assignment.ordermanagement.user.domain.port;

import com.assignment.ordermanagement.user.domain.model.User;

import java.util.Optional;

/**
 * Port (Interface) for User Repository
 */
public interface UserRepository {

    Optional<User> findByUsername(String username);
    
}

