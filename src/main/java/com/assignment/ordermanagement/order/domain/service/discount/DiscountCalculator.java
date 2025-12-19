package com.assignment.ordermanagement.order.domain.service.discount;

import com.assignment.ordermanagement.order.domain.model.OrderContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * Calculates total discount by applying all strategies
 */
public class DiscountCalculator {

    private final List<DiscountStrategy> strategies;

    public DiscountCalculator(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public BigDecimal calculate(OrderContext context) {
        return strategies.stream()
                .map(strategy -> strategy.calculate(context))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

