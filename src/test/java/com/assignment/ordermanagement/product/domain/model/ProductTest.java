package com.assignment.ordermanagement.product.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void shouldCreateProductWithRequiredFields() {
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.00");
        Integer quantity = 5;

        Product product = new Product(name, description, price, quantity);

        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getPrice()).isEqualByComparingTo(price);
        assertThat(product.getQuantity()).isEqualTo(quantity);
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.isDeleted()).isFalse();
    }

    @Test
    void shouldCreateProductWithAllFields() {
        Long id = 1L;
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.00");
        Integer quantity = 5;
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();
        boolean deleted = false;

        Product product = new Product(id, name, description, price, quantity, createdAt, updatedAt, deleted);

        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getPrice()).isEqualByComparingTo(price);
        assertThat(product.getQuantity()).isEqualTo(quantity);
        assertThat(product.getCreatedAt()).isEqualTo(createdAt);
        assertThat(product.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(product.isDeleted()).isEqualTo(deleted);
    }

    @Test
    void shouldReturnTrueWhenProductIsInStock() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 5);

        assertThat(product.isInStock()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenProductHasZeroQuantity() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 0);

        assertThat(product.isInStock()).isFalse();
    }

    @Test
    void shouldReturnFalseWhenProductHasNullQuantity() {
        Product product = new Product(1L, "Product", "Description", new BigDecimal("10.00"), 
            null, Instant.now(), null, false);

        assertThat(product.isInStock()).isFalse();
    }

    @Test
    void shouldReturnTrueWhenHasSufficientStock() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 10);

        assertThat(product.hasStock(5)).isTrue();
    }

    @Test
    void shouldReturnTrueWhenStockEqualsRequestedQuantity() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 5);

        assertThat(product.hasStock(5)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenInsufficientStock() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 3);

        assertThat(product.hasStock(5)).isFalse();
    }

    @Test
    void shouldDecreaseStockSuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 10);

        product.decreaseStock(3);

        assertThat(product.getQuantity()).isEqualTo(7);
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenDecreasingStockBeyondAvailable() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 3);

        assertThatThrownBy(() -> product.decreaseStock(5))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Insufficient stock");
    }

    @Test
    void shouldIncreaseStockSuccessfully() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 5);

        product.increaseStock(3);

        assertThat(product.getQuantity()).isEqualTo(8);
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenIncreasingStockByZero() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 5);

        assertThatThrownBy(() -> product.increaseStock(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Stock increase amount must be positive");
    }

    @Test
    void shouldThrowExceptionWhenIncreasingStockByNegativeAmount() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 5);

        assertThatThrownBy(() -> product.increaseStock(-3))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Stock increase amount must be positive");
    }

    @Test
    void shouldUpdateProductDetails() {
        Product product = new Product("Old Name", "Old Description", new BigDecimal("10.00"), 5);

        product.updateDetails("New Name", "New Description", new BigDecimal("20.00"), 10);

        assertThat(product.getName()).isEqualTo("New Name");
        assertThat(product.getDescription()).isEqualTo("New Description");
        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(product.getQuantity()).isEqualTo(10);
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldNotUpdateNameWhenNull() {
        Product product = new Product("Old Name", "Description", new BigDecimal("10.00"), 5);

        product.updateDetails(null, "New Description", new BigDecimal("20.00"), 10);

        assertThat(product.getName()).isEqualTo("Old Name");
    }

    @Test
    void shouldNotUpdateNameWhenBlank() {
        Product product = new Product("Old Name", "Description", new BigDecimal("10.00"), 5);

        product.updateDetails("  ", "New Description", new BigDecimal("20.00"), 10);

        assertThat(product.getName()).isEqualTo("Old Name");
    }

    @Test
    void shouldNotUpdatePriceWhenNegative() {
        Product product = new Product("Name", "Description", new BigDecimal("10.00"), 5);

        product.updateDetails("Name", "Description", new BigDecimal("-5.00"), 10);

        assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("10.00"));
    }

    @Test
    void shouldUpdatePriceWhenZero() {
        Product product = new Product("Name", "Description", new BigDecimal("10.00"), 5);

        product.updateDetails("Name", "Description", BigDecimal.ZERO, 10);

        assertThat(product.getPrice()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldNotUpdateQuantityWhenNegative() {
        Product product = new Product("Name", "Description", new BigDecimal("10.00"), 5);

        product.updateDetails("Name", "Description", new BigDecimal("10.00"), -5);

        assertThat(product.getQuantity()).isEqualTo(5);
    }

    @Test
    void shouldMarkProductAsDeleted() {
        Product product = new Product("Name", "Description", new BigDecimal("10.00"), 5);

        product.markAsDeleted();

        assertThat(product.isDeleted()).isTrue();
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldSetId() {
        Product product = new Product("Name", "Description", new BigDecimal("10.00"), 5);

        product.setId(100L);

        assertThat(product.getId()).isEqualTo(100L);
    }
}

