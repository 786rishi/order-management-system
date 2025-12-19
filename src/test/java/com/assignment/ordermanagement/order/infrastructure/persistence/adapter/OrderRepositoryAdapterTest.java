package com.assignment.ordermanagement.order.infrastructure.persistence.adapter;

import com.assignment.ordermanagement.order.domain.model.Order;
import com.assignment.ordermanagement.order.infrastructure.persistence.entity.OrderEntity;
import com.assignment.ordermanagement.order.infrastructure.persistence.mapper.OrderEntityMapper;
import com.assignment.ordermanagement.order.infrastructure.persistence.repository.OrderRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryAdapterTest {

    @Mock
    private OrderRepositoryJpa jpaRepository;

    @Mock
    private OrderEntityMapper mapper;

    private OrderRepositoryAdapter orderRepositoryAdapter;

    @BeforeEach
    void setUp() {
        orderRepositoryAdapter = new OrderRepositoryAdapter(jpaRepository, mapper);
    }

    @Test
    void shouldSaveOrder() {
        Order order = new Order(1L);
        OrderEntity entity = new OrderEntity();
        entity.setId(1L);
        OrderEntity savedEntity = new OrderEntity();
        savedEntity.setId(1L);
        Order savedOrder = new Order(1L, 1L, null, BigDecimal.ZERO, Instant.now());

        when(mapper.toEntity(order)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedOrder);

        Order result = orderRepositoryAdapter.save(order);

        assertThat(result).isNotNull();
        verify(mapper).toEntity(order);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }
}

