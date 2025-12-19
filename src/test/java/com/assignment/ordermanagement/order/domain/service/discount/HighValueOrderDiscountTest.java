package com.assignment.ordermanagement.order.domain.service.discount;

import com.assignment.ordermanagement.order.domain.model.OrderContext;
import com.assignment.ordermanagement.user.domain.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class HighValueOrderDiscountTest {

    private HighValueOrderDiscount discount;

    @BeforeEach
    void setUp() {
        discount = new HighValueOrderDiscount();
    }

    @Test
    void shouldApply5PercentDiscountWhenOrderIsAboveThreshold() {
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("500.00"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(new BigDecimal("25.00"));
    }

    @Test
    void shouldApply5PercentDiscountWhenOrderIsEqualToThreshold() {
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("500.00"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(new BigDecimal("25.00"));
    }

    @Test
    void shouldNotApplyDiscountWhenOrderIsBelowThreshold() {
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("499.99"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldApplyDiscountForHighValueOrder() {
        OrderContext context = new OrderContext(Role.PREMIUM_USER, new BigDecimal("1000.00"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    void shouldRoundDiscountCorrectly() {
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("533.33"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(new BigDecimal("26.67"));
    }
}

