package com.assignment.ordermanagement.product.application.usecase;

import com.assignment.ordermanagement.product.domain.port.ProductService;

/**
 * Use Case: Delete a product
 */
public class DeleteProductUseCase {

    private final ProductService productService;

    public DeleteProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public void execute(Long id) {
        productService.deleteProduct(id);
    }
}

