package com.assignment.ordermanagement.user.infrastructure.persistence.adapter;

import com.assignment.ordermanagement.user.domain.model.Role;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.infrastructure.persistence.entity.UserEntity;
import com.assignment.ordermanagement.user.infrastructure.persistence.mapper.UserEntityMapper;
import com.assignment.ordermanagement.user.infrastructure.persistence.repository.UserRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserRepositoryJpa jpaRepository;

    @Mock
    private UserEntityMapper mapper;

    private UserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void setUp() {
        userRepositoryAdapter = new UserRepositoryAdapter(jpaRepository, mapper);
    }

    @Test
    void shouldFindUserByUsername() {
        String username = "testuser";
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        User user = new User(1L, username, "encodedPassword", Role.USER);

        when(jpaRepository.findByUsername(username)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = userRepositoryAdapter.findByUsername(username);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        verify(jpaRepository).findByUsername(username);
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        String username = "nonexistent";

        when(jpaRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryAdapter.findByUsername(username);

        assertThat(result).isEmpty();
        verify(jpaRepository).findByUsername(username);
    }
}

