package com.assignment.ordermanagement.product.application.usecase;

import com.assignment.ordermanagement.product.domain.port.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteProductUseCaseTest {

    @Mock
    private ProductService productService;

    private DeleteProductUseCase deleteProductUseCase;

    @BeforeEach
    void setUp() {
        deleteProductUseCase = new DeleteProductUseCase(productService);
    }

    @Test
    void shouldDeleteProduct() {
        Long id = 1L;

        deleteProductUseCase.execute(id);

        verify(productService).deleteProduct(id);
    }
}

