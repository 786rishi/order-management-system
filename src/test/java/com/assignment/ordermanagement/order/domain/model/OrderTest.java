package com.assignment.ordermanagement.order.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void shouldCreateOrderWithUserId() {
        Long userId = 1L;
        
        Order order = new Order(userId);
        
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getOrderTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldCreateOrderWithAllFields() {
        Long id = 1L;
        Long userId = 2L;
        List<OrderItem> items = Arrays.asList(
            new OrderItem(1L, 2, new BigDecimal("10.00"))
        );
        BigDecimal orderTotal = new BigDecimal("20.00");
        Instant createdAt = Instant.now();

        Order order = new Order(id, userId, items, orderTotal, createdAt);

        assertThat(order.getId()).isEqualTo(id);
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getOrderTotal()).isEqualByComparingTo(orderTotal);
        assertThat(order.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldHandleNullItemsInConstructor() {
        Long id = 1L;
        Long userId = 2L;
        BigDecimal orderTotal = new BigDecimal("0.00");
        Instant createdAt = Instant.now();

        Order order = new Order(id, userId, null, orderTotal, createdAt);

        assertThat(order.getItems()).isEmpty();
    }

    @Test
    void shouldAddItemsAndRecalculateTotal() {
        Order order = new Order(1L);
        List<OrderItem> items = Arrays.asList(
            new OrderItem(1L, 2, new BigDecimal("10.00")),
            new OrderItem(2L, 1, new BigDecimal("15.00"))
        );

        order.addItems(items);

        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getOrderTotal()).isEqualByComparingTo(new BigDecimal("35.00"));
    }

    @Test
    void shouldCalculateSubtotal() {
        Order order = new Order(1L);
        List<OrderItem> items = Arrays.asList(
            new OrderItem(1L, 3, new BigDecimal("10.00")),
            new OrderItem(2L, 2, new BigDecimal("20.00"))
        );
        order.addItems(items);

        BigDecimal subtotal = order.calculateSubtotal();

        assertThat(subtotal).isEqualByComparingTo(new BigDecimal("70.00"));
    }

    @Test
    void shouldReturnUnmodifiableListOfItems() {
        Order order = new Order(1L);
        List<OrderItem> items = Arrays.asList(
            new OrderItem(1L, 2, new BigDecimal("10.00"))
        );
        order.addItems(items);

        List<OrderItem> retrievedItems = order.getItems();

        assertThat(retrievedItems).isInstanceOf(java.util.Collections.unmodifiableList(new ArrayList<>()).getClass());
    }

    @Test
    void shouldSetId() {
        Order order = new Order(1L);
        
        order.setId(100L);
        
        assertThat(order.getId()).isEqualTo(100L);
    }

    @Test
    void shouldSetOrderTotal() {
        Order order = new Order(1L);
        BigDecimal newTotal = new BigDecimal("50.00");
        
        order.setOrderTotal(newTotal);
        
        assertThat(order.getOrderTotal()).isEqualByComparingTo(newTotal);
    }
}

