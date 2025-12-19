package com.assignment.ordermanagement.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemWithValidData() {
        Long productId = 1L;
        Integer quantity = 5;
        BigDecimal unitPrice = new BigDecimal("10.00");

        OrderItem orderItem = new OrderItem(productId, quantity, unitPrice);

        assertThat(orderItem.getProductId()).isEqualTo(productId);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getUnitPrice()).isEqualByComparingTo(unitPrice);
        assertThat(orderItem.getDiscountApplied()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(orderItem.getTotalPrice()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        assertThatThrownBy(() -> new OrderItem(1L, 0, new BigDecimal("10.00")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        assertThatThrownBy(() -> new OrderItem(1L, -5, new BigDecimal("10.00")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    void shouldThrowExceptionWhenUnitPriceIsNegative() {
        assertThatThrownBy(() -> new OrderItem(1L, 5, new BigDecimal("-10.00")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Unit price cannot be negative");
    }

    @Test
    void shouldCreateOrderItemWithAllFields() {
        Long id = 1L;
        Long productId = 2L;
        Integer quantity = 3;
        BigDecimal unitPrice = new BigDecimal("20.00");
        BigDecimal discountApplied = new BigDecimal("5.00");
        BigDecimal totalPrice = new BigDecimal("60.00");

        OrderItem orderItem = new OrderItem(id, productId, quantity, unitPrice, discountApplied, totalPrice);

        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getProductId()).isEqualTo(productId);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getUnitPrice()).isEqualByComparingTo(unitPrice);
        assertThat(orderItem.getDiscountApplied()).isEqualByComparingTo(discountApplied);
        assertThat(orderItem.getTotalPrice()).isEqualByComparingTo(totalPrice);
    }

    @Test
    void shouldHandleNullDiscountInFullConstructor() {
        OrderItem orderItem = new OrderItem(1L, 2L, 3, new BigDecimal("20.00"), null, new BigDecimal("60.00"));

        assertThat(orderItem.getDiscountApplied()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldApplyDiscount() {
        OrderItem orderItem = new OrderItem(1L, 2, new BigDecimal("10.00"));
        BigDecimal discount = new BigDecimal("5.00");

        orderItem.applyDiscount(discount);

        assertThat(orderItem.getDiscountApplied()).isEqualByComparingTo(discount);
    }

    @Test
    void shouldThrowExceptionWhenDiscountIsNegative() {
        OrderItem orderItem = new OrderItem(1L, 2, new BigDecimal("10.00"));

        assertThatThrownBy(() -> orderItem.applyDiscount(new BigDecimal("-5.00")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Discount cannot be negative");
    }

    @Test
    void shouldCalculateFinalPrice() {
        OrderItem orderItem = new OrderItem(1L, 2, new BigDecimal("10.00"));
        orderItem.applyDiscount(new BigDecimal("5.00"));

        BigDecimal finalPrice = orderItem.calculateFinalPrice();

        assertThat(finalPrice).isEqualByComparingTo(new BigDecimal("15.00"));
    }

    @Test
    void shouldSetId() {
        OrderItem orderItem = new OrderItem(1L, 2, new BigDecimal("10.00"));
        
        orderItem.setId(100L);
        
        assertThat(orderItem.getId()).isEqualTo(100L);
    }
}

