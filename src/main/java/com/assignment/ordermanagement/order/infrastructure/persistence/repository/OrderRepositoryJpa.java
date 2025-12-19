package com.assignment.ordermanagement.order.infrastructure.persistence.repository;

import com.assignment.ordermanagement.order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository for OrderEntity
 */
public interface OrderRepositoryJpa extends JpaRepository<OrderEntity, Long> {
}

