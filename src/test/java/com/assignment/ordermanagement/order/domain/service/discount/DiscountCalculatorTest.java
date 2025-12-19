package com.assignment.ordermanagement.order.domain.service.discount;

import com.assignment.ordermanagement.order.domain.model.OrderContext;
import com.assignment.ordermanagement.user.domain.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountCalculatorTest {

    @Mock
    private DiscountStrategy strategy1;

    @Mock
    private DiscountStrategy strategy2;

    private DiscountCalculator calculator;

    @Test
    void shouldCalculateTotalDiscountFromMultipleStrategies() {
        List<DiscountStrategy> strategies = Arrays.asList(strategy1, strategy2);
        calculator = new DiscountCalculator(strategies);
        OrderContext context = new OrderContext(Role.PREMIUM_USER, new BigDecimal("1000.00"));

        when(strategy1.calculate(context)).thenReturn(new BigDecimal("100.00"));
        when(strategy2.calculate(context)).thenReturn(new BigDecimal("50.00"));

        BigDecimal totalDiscount = calculator.calculate(context);

        assertThat(totalDiscount).isEqualByComparingTo(new BigDecimal("150.00"));
    }

    @Test
    void shouldReturnZeroWhenNoStrategies() {
        calculator = new DiscountCalculator(Collections.emptyList());
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("100.00"));

        BigDecimal totalDiscount = calculator.calculate(context);

        assertThat(totalDiscount).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldHandleStrategyReturningZero() {
        List<DiscountStrategy> strategies = Arrays.asList(strategy1, strategy2);
        calculator = new DiscountCalculator(strategies);
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("100.00"));

        when(strategy1.calculate(context)).thenReturn(BigDecimal.ZERO);
        when(strategy2.calculate(context)).thenReturn(new BigDecimal("10.00"));

        BigDecimal totalDiscount = calculator.calculate(context);

        assertThat(totalDiscount).isEqualByComparingTo(new BigDecimal("10.00"));
    }
}

