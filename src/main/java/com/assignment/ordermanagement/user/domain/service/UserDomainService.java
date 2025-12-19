package com.assignment.ordermanagement.user.domain.service;

import com.assignment.ordermanagement.user.domain.exception.UserNotFoundException;
import com.assignment.ordermanagement.user.domain.model.Role;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.PasswordEncoder;
import com.assignment.ordermanagement.user.domain.port.UserRepository;
import com.assignment.ordermanagement.user.domain.port.UserService;

/**
 * User Domain Service - Core business logic for users
 */
public class UserDomainService implements UserService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

}

