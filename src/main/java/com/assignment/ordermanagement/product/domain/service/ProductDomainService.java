package com.assignment.ordermanagement.product.domain.service;

import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductRepository;
import com.assignment.ordermanagement.product.domain.port.ProductService;
import com.assignment.ordermanagement.product.domain.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product Domain Service Implementation
 * Contains core business logic
 */
public class ProductDomainService implements ProductService {

    private final ProductRepository productRepository;

    public ProductDomainService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(String name, String description, BigDecimal price, Integer quantity) {
        validateProductData(name, price, quantity);
        
        Product product = new Product(name, description, price, quantity);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, String name, String description, BigDecimal price, Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        
        product.updateDetails(name, description, price, quantity);
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        return productRepository.search(name, minPrice, maxPrice, inStock);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    @Override
    public void decreaseStock(Long productId, int quantity) {
        Product product = getProductById(productId);
        product.decreaseStock(quantity);
        productRepository.save(product);
    }

    private void validateProductData(String name, BigDecimal price, Integer quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Product quantity must be non-negative");
        }
    }
}

