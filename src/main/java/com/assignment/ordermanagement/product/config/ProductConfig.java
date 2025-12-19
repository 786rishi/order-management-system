package com.assignment.ordermanagement.product.config;

import com.assignment.ordermanagement.product.application.usecase.*;
import com.assignment.ordermanagement.product.domain.port.ProductRepository;
import com.assignment.ordermanagement.product.domain.port.ProductService;
import com.assignment.ordermanagement.product.domain.service.ProductDomainService;
import com.assignment.ordermanagement.product.infrastructure.cache.CachingProductRepositoryAdapter;
import com.assignment.ordermanagement.product.infrastructure.persistence.adapter.ProductRepositoryAdapter;
import com.assignment.ordermanagement.product.infrastructure.persistence.mapper.ProductEntityMapper;
import com.assignment.ordermanagement.product.infrastructure.persistence.repository.ProductRepositoryJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Product feature
 * Wires all layers together following dependency inversion
 */
@Configuration
public class ProductConfig {

    // Infrastructure Layer
    @Bean
    public ProductEntityMapper productEntityMapper() {
        return new ProductEntityMapper();
    }

    @Bean
    public ProductRepository productRepository(ProductRepositoryJpa jpaRepository, 
                                               ProductEntityMapper mapper) {
        ProductRepositoryAdapter adapter = new ProductRepositoryAdapter(jpaRepository, mapper);
        return new CachingProductRepositoryAdapter(adapter);
    }

    // Domain Layer
    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductDomainService(productRepository);
    }

    // Application Layer - Use Cases
    @Bean
    public CreateProductUseCase createProductUseCase(ProductService productService) {
        return new CreateProductUseCase(productService);
    }

    @Bean
    public GetAllProductsUseCase getAllProductsUseCase(ProductService productService) {
        return new GetAllProductsUseCase(productService);
    }

    @Bean
    public SearchProductsUseCase searchProductsUseCase(ProductService productService) {
        return new SearchProductsUseCase(productService);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductService productService) {
        return new UpdateProductUseCase(productService);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductService productService) {
        return new DeleteProductUseCase(productService);
    }
}

