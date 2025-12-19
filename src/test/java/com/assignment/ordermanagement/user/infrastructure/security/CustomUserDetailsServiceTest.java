package com.assignment.ordermanagement.user.infrastructure.security;

import com.assignment.ordermanagement.user.domain.exception.UserNotFoundException;
import com.assignment.ordermanagement.user.domain.model.Role;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void shouldLoadUserByUsername() {
        String username = "testuser";
        User user = new User(1L, username, "encodedPassword", Role.USER);

        when(userService.getUserByUsername(username)).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next()).isEqualTo(new SimpleGrantedAuthority("ROLE_USER"));
        verify(userService).getUserByUsername(username);
    }

    @Test
    void shouldLoadPremiumUser() {
        String username = "premiumuser";
        User user = new User(1L, username, "encodedPassword", Role.PREMIUM_USER);

        when(userService.getUserByUsername(username)).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next()).isEqualTo(new SimpleGrantedAuthority("ROLE_PREMIUM_USER"));
    }

    @Test
    void shouldLoadAdmin() {
        String username = "admin";
        User user = new User(1L, username, "encodedPassword", Role.ADMIN);

        when(userService.getUserByUsername(username)).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next()).isEqualTo(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "nonexistent";

        when(userService.getUserByUsername(username)).thenThrow(new UserNotFoundException("User not found"));

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(username))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("User not found");
    }
}

