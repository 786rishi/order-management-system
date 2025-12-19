package com.assignment.ordermanagement.order.domain.model;

import com.assignment.ordermanagement.user.domain.model.Role;

import java.math.BigDecimal;

/**
 * Context information for order discount calculation
 */
public record OrderContext(
        Role userRole,
        BigDecimal orderSubtotal
) {
}

