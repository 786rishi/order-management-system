package com.assignment.ordermanagement.order.domain.port;

import com.assignment.ordermanagement.order.domain.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * Port (Interface) for Order Repository
 */
public interface OrderRepository {
    
    Order save(Order order);

}

