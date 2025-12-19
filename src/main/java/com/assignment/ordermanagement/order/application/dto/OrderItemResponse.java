package com.assignment.ordermanagement.order.application.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal discountApplied,
        BigDecimal totalPrice
) {
}

