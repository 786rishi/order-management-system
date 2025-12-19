package com.assignment.ordermanagement.order.domain.service;

import com.assignment.ordermanagement.order.domain.exception.OrderNotFoundException;
import com.assignment.ordermanagement.order.domain.model.Order;
import com.assignment.ordermanagement.order.domain.port.OrderRepository;

import java.util.List;

/**
 * Order Domain Service - Core business logic for orders
 */
public class OrderDomainService {

    private final OrderRepository orderRepository;

    public OrderDomainService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

}

