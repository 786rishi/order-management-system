package com.assignment.ordermanagement.product.application.usecase;

import com.assignment.ordermanagement.product.application.dto.ProductRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductService productService;

    private CreateProductUseCase createProductUseCase;

    @BeforeEach
    void setUp() {
        createProductUseCase = new CreateProductUseCase(productService);
    }

    @Test
    void shouldCreateProduct() {
        ProductRequest request = new ProductRequest("Product", "Description", new BigDecimal("10.00"), 5);
        Product product = new Product(1L, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);

        when(productService.createProduct("Product", "Description", new BigDecimal("10.00"), 5))
            .thenReturn(product);

        ProductResponse response = createProductUseCase.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Product");
        assertThat(response.description()).isEqualTo("Description");
        assertThat(response.price()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(response.quantity()).isEqualTo(5);
        assertThat(response.inStock()).isTrue();
        verify(productService).createProduct("Product", "Description", new BigDecimal("10.00"), 5);
    }

    @Test
    void shouldCreateProductWithZeroQuantity() {
        ProductRequest request = new ProductRequest("Product", "Description", new BigDecimal("10.00"), 0);
        Product product = new Product(1L, "Product", "Description", new BigDecimal("10.00"), 0, Instant.now(), null, false);

        when(productService.createProduct(any(), any(), any(), any())).thenReturn(product);

        ProductResponse response = createProductUseCase.execute(request);

        assertThat(response.inStock()).isFalse();
    }
}

