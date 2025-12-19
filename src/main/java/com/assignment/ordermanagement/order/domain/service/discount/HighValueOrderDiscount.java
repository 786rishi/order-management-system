package com.assignment.ordermanagement.order.domain.service.discount;

import com.assignment.ordermanagement.order.domain.model.OrderContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Discount for high-value orders - 5% off for orders over $1000
 */
public class HighValueOrderDiscount implements DiscountStrategy {

    private static final BigDecimal THRESHOLD = new BigDecimal("500.00");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.05"); // 5%

    @Override
    public BigDecimal calculate(OrderContext context) {
        if (context.orderSubtotal().compareTo(THRESHOLD) >= 0) {
            return context.orderSubtotal()
                    .multiply(DISCOUNT_RATE)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}

