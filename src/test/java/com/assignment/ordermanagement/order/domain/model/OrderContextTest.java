package com.assignment.ordermanagement.order.domain.model;

import com.assignment.ordermanagement.user.domain.model.Role;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderContextTest {

    @Test
    void shouldCreateOrderContext() {
        Role role = Role.PREMIUM_USER;
        BigDecimal subtotal = new BigDecimal("100.00");

        OrderContext context = new OrderContext(role, subtotal);

        assertThat(context.userRole()).isEqualTo(role);
        assertThat(context.orderSubtotal()).isEqualByComparingTo(subtotal);
    }

    @Test
    void shouldCreateOrderContextWithUserRole() {
        OrderContext context = new OrderContext(Role.USER, new BigDecimal("50.00"));

        assertThat(context.userRole()).isEqualTo(Role.USER);
        assertThat(context.orderSubtotal()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    void shouldCreateOrderContextWithAdminRole() {
        OrderContext context = new OrderContext(Role.ADMIN, new BigDecimal("200.00"));

        assertThat(context.userRole()).isEqualTo(Role.ADMIN);
        assertThat(context.orderSubtotal()).isEqualByComparingTo(new BigDecimal("200.00"));
    }
}

