package com.assignment.ordermanagement.order.infrastructure.persistence.adapter;

import com.assignment.ordermanagement.order.domain.model.Order;
import com.assignment.ordermanagement.order.domain.port.OrderRepository;
import com.assignment.ordermanagement.order.infrastructure.persistence.entity.OrderEntity;
import com.assignment.ordermanagement.order.infrastructure.persistence.mapper.OrderEntityMapper;
import com.assignment.ordermanagement.order.infrastructure.persistence.repository.OrderRepositoryJpa;

/**
 * Adapter that implements the domain OrderRepository port using JPA
 */
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderRepositoryJpa jpaRepository;
    private final OrderEntityMapper mapper;

    public OrderRepositoryAdapter(OrderRepositoryJpa jpaRepository, OrderEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}

