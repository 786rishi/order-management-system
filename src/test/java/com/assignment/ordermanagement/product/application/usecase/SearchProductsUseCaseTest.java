package com.assignment.ordermanagement.product.application.usecase;

import com.assignment.ordermanagement.product.application.dto.ProductResponse;
import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProductsUseCaseTest {

    @Mock
    private ProductService productService;

    private SearchProductsUseCase searchProductsUseCase;

    @BeforeEach
    void setUp() {
        searchProductsUseCase = new SearchProductsUseCase(productService);
    }

    @Test
    void shouldSearchProductsByName() {
        String name = "Test";
        List<Product> products = Arrays.asList(
            new Product(1L, "Test Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false)
        );

        when(productService.searchProducts(name, null, null, null)).thenReturn(products);

        List<ProductResponse> responses = searchProductsUseCase.execute(name, null, null, null);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).contains("Test");
        verify(productService).searchProducts(name, null, null, null);
    }

    @Test
    void shouldSearchProductsByPriceRange() {
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");
        List<Product> products = Arrays.asList(
            new Product(1L, "Product", "Description", new BigDecimal("25.00"), 5, Instant.now(), null, false)
        );

        when(productService.searchProducts(null, minPrice, maxPrice, null)).thenReturn(products);

        List<ProductResponse> responses = searchProductsUseCase.execute(null, minPrice, maxPrice, null);

        assertThat(responses).hasSize(1);
        verify(productService).searchProducts(null, minPrice, maxPrice, null);
    }

    @Test
    void shouldSearchInStockProducts() {
        Boolean inStock = true;
        List<Product> products = Arrays.asList(
            new Product(1L, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false)
        );

        when(productService.searchProducts(null, null, null, inStock)).thenReturn(products);

        List<ProductResponse> responses = searchProductsUseCase.execute(null, null, null, inStock);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).inStock()).isTrue();
        verify(productService).searchProducts(null, null, null, inStock);
    }

    @Test
    void shouldSearchWithMultipleCriteria() {
        String name = "Test";
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");
        Boolean inStock = true;

        List<Product> products = Arrays.asList(
            new Product(1L, "Test Product", "Description", new BigDecimal("25.00"), 5, Instant.now(), null, false)
        );

        when(productService.searchProducts(name, minPrice, maxPrice, inStock)).thenReturn(products);

        List<ProductResponse> responses = searchProductsUseCase.execute(name, minPrice, maxPrice, inStock);

        assertThat(responses).hasSize(1);
        verify(productService).searchProducts(name, minPrice, maxPrice, inStock);
    }
}

