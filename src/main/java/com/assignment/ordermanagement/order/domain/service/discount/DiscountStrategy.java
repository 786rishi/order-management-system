package com.assignment.ordermanagement.order.domain.service.discount;

import com.assignment.ordermanagement.order.domain.model.OrderContext;

import java.math.BigDecimal;

/**
 * Strategy interface for calculating discounts
 */
public interface DiscountStrategy {
    BigDecimal calculate(OrderContext context);
}

