package com.assignment.ordermanagement.order.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        List<OrderItemResponse> items,
        BigDecimal orderTotal,
        Instant createdAt
) {
}

