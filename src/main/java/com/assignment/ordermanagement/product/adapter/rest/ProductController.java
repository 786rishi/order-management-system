package com.assignment.ordermanagement.product.adapter.rest;

import com.assignment.ordermanagement.product.application.dto.ProductRequest;
import com.assignment.ordermanagement.product.application.dto.ProductResponse;
import com.assignment.ordermanagement.product.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Product operations
 * Authorization handled at adapter layer following Clean Architecture principles
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final SearchProductsUseCase searchProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase,
                           GetAllProductsUseCase getAllProductsUseCase,
                           SearchProductsUseCase searchProductsUseCase,
                           UpdateProductUseCase updateProductUseCase,
                           DeleteProductUseCase deleteProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
        this.searchProductsUseCase = searchProductsUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new product", security = @SecurityRequirement(name = "bearer-jwt"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = createProductUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all products (Public access)")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = getAllProductsUseCase.execute();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products with filters")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock) {
        
        List<ProductResponse> products = searchProductsUseCase.execute(name, minPrice, maxPrice, inStock);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", security = @SecurityRequirement(name = "bearer-jwt"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        
        ProductResponse response = updateProductUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", security = @SecurityRequirement(name = "bearer-jwt"))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

