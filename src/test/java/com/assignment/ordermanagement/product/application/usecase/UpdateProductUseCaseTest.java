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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductUseCaseTest {

    @Mock
    private ProductService productService;

    private UpdateProductUseCase updateProductUseCase;

    @BeforeEach
    void setUp() {
        updateProductUseCase = new UpdateProductUseCase(productService);
    }

    @Test
    void shouldUpdateProduct() {
        Long id = 1L;
        ProductRequest request = new ProductRequest("Updated Product", "Updated Description", new BigDecimal("20.00"), 10);
        Product updatedProduct = new Product(id, "Updated Product", "Updated Description", 
            new BigDecimal("20.00"), 10, Instant.now(), Instant.now(), false);

        when(productService.updateProduct(id, "Updated Product", "Updated Description", new BigDecimal("20.00"), 10))
            .thenReturn(updatedProduct);

        ProductResponse response = updateProductUseCase.execute(id, request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Updated Product");
        assertThat(response.description()).isEqualTo("Updated Description");
        assertThat(response.price()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(response.quantity()).isEqualTo(10);
        verify(productService).updateProduct(id, "Updated Product", "Updated Description", new BigDecimal("20.00"), 10);
    }
}

