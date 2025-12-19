package com.assignment.ordermanagement.product.application.usecase;

import com.assignment.ordermanagement.product.application.dto.ProductResponse;
import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case: Search products with filters
 */
public class SearchProductsUseCase {

    private final ProductService productService;

    public SearchProductsUseCase(ProductService productService) {
        this.productService = productService;
    }

    public List<ProductResponse> execute(String name, BigDecimal minPrice, 
                                         BigDecimal maxPrice, Boolean inStock) {
        List<Product> products = productService.searchProducts(name, minPrice, maxPrice, inStock);
        
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

