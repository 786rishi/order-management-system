package com.assignment.ordermanagement.user.infrastructure.persistence.adapter;

import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.UserRepository;
import com.assignment.ordermanagement.user.infrastructure.persistence.mapper.UserEntityMapper;
import com.assignment.ordermanagement.user.infrastructure.persistence.repository.UserRepositoryJpa;
import java.util.Optional;

/**
 * Adapter that implements the domain UserRepository port using JPA
 */
public class UserRepositoryAdapter implements UserRepository {

    private final UserRepositoryJpa jpaRepository;
    private final UserEntityMapper mapper;

    public UserRepositoryAdapter(UserRepositoryJpa jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username)
            .map(mapper::toDomain);
    }

}

