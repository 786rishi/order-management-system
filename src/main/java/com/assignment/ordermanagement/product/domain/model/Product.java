package com.assignment.ordermanagement.product.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Product Domain Model - Pure business entity without framework dependencies
 */
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean deleted;

    // Constructor for creating new products
    public Product(String name, String description, BigDecimal price, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = Instant.now();
        this.deleted = false;
    }

    // Constructor for reconstructing from persistence
    public Product(Long id, String name, String description, BigDecimal price, 
                   Integer quantity, Instant createdAt, Instant updatedAt, boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleted = deleted;
    }

    // Business logic methods
    public boolean isInStock() {
        return quantity != null && quantity > 0;
    }

    public boolean hasStock(int requestedQuantity) {
        return quantity != null && quantity >= requestedQuantity;
    }

    public void decreaseStock(int amount) {
        if (!hasStock(amount)) {
            throw new IllegalStateException("Insufficient stock for product: " + name);
        }
        this.quantity -= amount;
        this.updatedAt = Instant.now();
    }

    public void increaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Stock increase amount must be positive");
        }
        this.quantity += amount;
        this.updatedAt = Instant.now();
    }

    public void updateDetails(String name, String description, BigDecimal price, Integer quantity) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (price != null && price.compareTo(BigDecimal.ZERO) >= 0) {
            this.price = price;
        }
        if (quantity != null && quantity >= 0) {
            this.quantity = quantity;
        }
        this.updatedAt = Instant.now();
    }

    public void markAsDeleted() {
        this.deleted = true;
        this.updatedAt = Instant.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    // For infrastructure layer to set ID after persistence
    public void setId(Long id) {
        this.id = id;
    }
}

