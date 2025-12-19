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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllProductsUseCaseTest {

    @Mock
    private ProductService productService;

    private GetAllProductsUseCase getAllProductsUseCase;

    @BeforeEach
    void setUp() {
        getAllProductsUseCase = new GetAllProductsUseCase(productService);
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = Arrays.asList(
            new Product(1L, "Product1", "Description1", new BigDecimal("10.00"), 5, Instant.now(), null, false),
            new Product(2L, "Product2", "Description2", new BigDecimal("20.00"), 10, Instant.now(), null, false)
        );

        when(productService.getAllProducts()).thenReturn(products);

        List<ProductResponse> responses = getAllProductsUseCase.execute();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).id()).isEqualTo(1L);
        assertThat(responses.get(1).id()).isEqualTo(2L);
        verify(productService).getAllProducts();
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        List<ProductResponse> responses = getAllProductsUseCase.execute();

        assertThat(responses).isEmpty();
    }
}

