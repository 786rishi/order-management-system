package com.assignment.ordermanagement.user.domain.service;

import com.assignment.ordermanagement.user.domain.exception.UserNotFoundException;
import com.assignment.ordermanagement.user.domain.model.Role;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDomainServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserDomainService userDomainService;

    @BeforeEach
    void setUp() {
        userDomainService = new UserDomainService(userRepository);
    }

    @Test
    void shouldGetUserByUsername() {
        String username = "testuser";
        User user = new User(1L, username, "password", Role.USER);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userDomainService.getUserByUsername(username);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "nonexistent";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDomainService.getUserByUsername(username))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("User not found with username: " + username);
    }
}

