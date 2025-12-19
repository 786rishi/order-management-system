package com.assignment.ordermanagement.product.domain.service;

import com.assignment.ordermanagement.product.domain.exception.ProductNotFoundException;
import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDomainServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductDomainService productDomainService;

    @BeforeEach
    void setUp() {
        productDomainService = new ProductDomainService(productRepository);
    }

    @Test
    void shouldCreateProduct() {
        String name = "Product";
        String description = "Description";
        BigDecimal price = new BigDecimal("10.00");
        Integer quantity = 5;
        Product savedProduct = new Product(1L, name, description, price, quantity, Instant.now(), null, false);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productDomainService.createProduct(name, description, price, quantity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithEmptyName() {
        assertThatThrownBy(() -> productDomainService.createProduct("", "Description", new BigDecimal("10.00"), 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product name cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNullName() {
        assertThatThrownBy(() -> productDomainService.createProduct(null, "Description", new BigDecimal("10.00"), 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product name cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNegativePrice() {
        assertThatThrownBy(() -> productDomainService.createProduct("Product", "Description", new BigDecimal("-10.00"), 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product price must be non-negative");
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNullPrice() {
        assertThatThrownBy(() -> productDomainService.createProduct("Product", "Description", null, 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product price must be non-negative");
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNegativeQuantity() {
        assertThatThrownBy(() -> productDomainService.createProduct("Product", "Description", new BigDecimal("10.00"), -5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product quantity must be non-negative");
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNullQuantity() {
        assertThatThrownBy(() -> productDomainService.createProduct("Product", "Description", new BigDecimal("10.00"), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Product quantity must be non-negative");
    }

    @Test
    void shouldUpdateProduct() {
        Long id = 1L;
        Product existingProduct = new Product(id, "Old Name", "Old Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);
        Product updatedProduct = new Product(id, "New Name", "New Description", new BigDecimal("20.00"), 10, Instant.now(), Instant.now(), false);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productDomainService.updateProduct(id, "New Name", "New Description", new BigDecimal("20.00"), 10);

        assertThat(result.getName()).isEqualTo("New Name");
        verify(productRepository).findById(id);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productDomainService.updateProduct(id, "Name", "Description", new BigDecimal("10.00"), 5))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Product not found with id: " + id);
    }

    @Test
    void shouldGetProductById() {
        Long id = 1L;
        Product product = new Product(id, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product result = productDomainService.getProductById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(productRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productDomainService.getProductById(id))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Product not found with id: " + id);
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = Arrays.asList(
            new Product(1L, "Product1", "Description1", new BigDecimal("10.00"), 5, Instant.now(), null, false),
            new Product(2L, "Product2", "Description2", new BigDecimal("20.00"), 10, Instant.now(), null, false)
        );

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productDomainService.getAllProducts();

        assertThat(result).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    void shouldSearchProducts() {
        String name = "Test";
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");
        Boolean inStock = true;

        List<Product> products = Arrays.asList(
            new Product(1L, "Test Product", "Description", new BigDecimal("25.00"), 5, Instant.now(), null, false)
        );

        when(productRepository.search(name, minPrice, maxPrice, inStock)).thenReturn(products);

        List<Product> result = productDomainService.searchProducts(name, minPrice, maxPrice, inStock);

        assertThat(result).hasSize(1);
        verify(productRepository).search(name, minPrice, maxPrice, inStock);
    }

    @Test
    void shouldDeleteProduct() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        productDomainService.deleteProduct(id);

        verify(productRepository).existsById(id);
        verify(productRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        Long id = 999L;
        when(productRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> productDomainService.deleteProduct(id))
            .isInstanceOf(ProductNotFoundException.class)
            .hasMessage("Product not found with id: " + id);

        verify(productRepository, never()).deleteById(id);
    }

    @Test
    void shouldDecreaseStock() {
        Long id = 1L;
        Product product = new Product(id, "Product", "Description", new BigDecimal("10.00"), 10, Instant.now(), null, false);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productDomainService.decreaseStock(id, 3);

        verify(productRepository).findById(id);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenDecreasingStockOfNonExistentProduct() {
        Long id = 999L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productDomainService.decreaseStock(id, 3))
            .isInstanceOf(ProductNotFoundException.class);
    }
}

