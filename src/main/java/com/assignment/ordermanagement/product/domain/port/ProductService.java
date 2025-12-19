package com.assignment.ordermanagement.product.domain.port;

import com.assignment.ordermanagement.product.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Port (Interface) for Product Domain Service
 */
public interface ProductService {
    
    Product createProduct(String name, String description, BigDecimal price, Integer quantity);
    
    Product updateProduct(Long id, String name, String description, BigDecimal price, Integer quantity);
    
    Product getProductById(Long id);
    
    List<Product> getAllProducts();
    
    List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock);
    
    void deleteProduct(Long id);
    
    void decreaseStock(Long productId, int quantity);
}

