package com.assignment.ordermanagement.order.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Order Domain Model - Pure business entity
 */
public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private BigDecimal orderTotal;
    private Instant createdAt;

    public Order(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.orderTotal = BigDecimal.ZERO;
        this.createdAt = Instant.now();
    }

    public Order(Long id, Long userId, List<OrderItem> items, BigDecimal orderTotal, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.orderTotal = orderTotal;
        this.createdAt = createdAt;
    }

    public void addItems(List<OrderItem> items) {
        this.items.addAll(items);
        recalculateTotal();
    }

    public BigDecimal calculateSubtotal() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void recalculateTotal() {
        this.orderTotal = calculateSubtotal();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }
}

