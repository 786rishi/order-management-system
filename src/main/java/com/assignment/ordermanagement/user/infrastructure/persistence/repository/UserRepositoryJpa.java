package com.assignment.ordermanagement.user.infrastructure.persistence.repository;

import com.assignment.ordermanagement.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for UserEntity
 */
public interface UserRepositoryJpa extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    boolean existsByUsername(String username);
}

