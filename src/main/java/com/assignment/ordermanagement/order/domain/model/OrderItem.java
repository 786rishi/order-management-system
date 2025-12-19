package com.assignment.ordermanagement.order.domain.model;

import java.math.BigDecimal;

/**
 * OrderItem Domain Model
 */
public class OrderItem {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountApplied;
    private BigDecimal totalPrice;

    public OrderItem(Long productId, Integer quantity, BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountApplied = BigDecimal.ZERO;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Constructor for reconstructing from persistence
    public OrderItem(Long id, Long productId, Integer quantity, BigDecimal unitPrice, 
                    BigDecimal discountApplied, BigDecimal totalPrice) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountApplied = discountApplied != null ? discountApplied : BigDecimal.ZERO;
        this.totalPrice = totalPrice;
    }

    public void applyDiscount(BigDecimal discount) {
        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        this.discountApplied = discount;
    }

    public BigDecimal calculateFinalPrice() {
        return totalPrice.subtract(discountApplied);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getDiscountApplied() {
        return discountApplied;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

