package com.assignment.ordermanagement.order.domain.service.discount;

import com.assignment.ordermanagement.order.domain.model.OrderContext;
import com.assignment.ordermanagement.user.domain.model.Role;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Discount for premium users - 10% off
 */
public class PremiumUserDiscount implements DiscountStrategy {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10"); // 10%

    @Override
    public BigDecimal calculate(OrderContext context) {
        if (context.userRole() == Role.PREMIUM_USER) {
            return context.orderSubtotal()
                    .multiply(DISCOUNT_RATE)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}

