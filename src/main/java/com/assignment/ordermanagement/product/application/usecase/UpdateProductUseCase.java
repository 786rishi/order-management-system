package com.assignment.ordermanagement.product.application.usecase;

import com.assignment.ordermanagement.product.application.dto.ProductRequest;
import com.assignment.ordermanagement.product.application.dto.ProductResponse;
import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductService;

/**
 * Use Case: Update an existing product
 */
public class UpdateProductUseCase {

    private final ProductService productService;

    public UpdateProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public ProductResponse execute(Long id, ProductRequest request) {
        Product product = productService.updateProduct(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.quantity()
        );
        
        return mapToResponse(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.isInStock()
        );
    }
}

