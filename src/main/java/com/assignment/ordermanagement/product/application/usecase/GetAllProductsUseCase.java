package com.assignment.ordermanagement.product.application.usecase;

import com.assignment.ordermanagement.product.application.dto.ProductResponse;
import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case: Get all products
 */
public class GetAllProductsUseCase {

    private final ProductService productService;

    public GetAllProductsUseCase(ProductService productService) {
        this.productService = productService;
    }

    public List<ProductResponse> execute() {
        List<Product> products = productService.getAllProducts();
        
        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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

