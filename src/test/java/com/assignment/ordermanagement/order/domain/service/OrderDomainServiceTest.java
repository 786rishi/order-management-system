package com.assignment.ordermanagement.order.domain.service;

import com.assignment.ordermanagement.order.domain.model.Order;
import com.assignment.ordermanagement.order.domain.port.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDomainServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderDomainService orderDomainService;

    @BeforeEach
    void setUp() {
        orderDomainService = new OrderDomainService(orderRepository);
    }

    @Test
    void shouldCreateOrder() {
        Order order = new Order(1L);
        Order savedOrder = new Order(1L);
        savedOrder.setId(100L);

        when(orderRepository.save(order)).thenReturn(savedOrder);

        Order result = orderDomainService.createOrder(order);

        assertThat(result).isEqualTo(savedOrder);
        assertThat(result.getId()).isEqualTo(100L);
        verify(orderRepository).save(order);
    }
}

