package com.assignment.ordermanagement.user.infrastructure.persistence.mapper;

import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.infrastructure.persistence.entity.UserEntity;

/**
 * Mapper between User Domain Model and JPA Entity
 */
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        
        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getRole()
        );
    }
}

