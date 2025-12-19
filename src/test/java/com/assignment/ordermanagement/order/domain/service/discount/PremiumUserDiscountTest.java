package com.assignment.ordermanagement.order.domain.service.discount;

import com.assignment.ordermanagement.order.domain.model.OrderContext;
import com.assignment.ordermanagement.user.domain.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PremiumUserDiscountTest {

    private PremiumUserDiscount discount;

    @BeforeEach
    void setUp() {
        discount = new PremiumUserDiscount();
    }

    @Test
    void shouldApply10PercentDiscountForPremiumUser() {
        OrderContext context = new OrderContext(Role.PREMIUM_USER, new BigDecimal("100.00"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(new BigDecimal("10.00"));
    }

    @Test
    void shouldNotApplyDiscountForRegularUser() {
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("100.00"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldNotApplyDiscountForAdmin() {
        OrderContext context = new OrderContext(Role.ADMIN, new BigDecimal("100.00"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateCorrectDiscountForLargeOrder() {
        OrderContext context = new OrderContext(Role.PREMIUM_USER, new BigDecimal("1000.00"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    void shouldRoundDiscountCorrectly() {
        OrderContext context = new OrderContext(Role.PREMIUM_USER, new BigDecimal("33.33"));

        BigDecimal result = discount.calculate(context);

        assertThat(result).isEqualByComparingTo(new BigDecimal("3.33"));
    }
}

