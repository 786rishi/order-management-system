package com.assignment.ordermanagement.product.domain.port;

import com.assignment.ordermanagement.product.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Port (Interface) for Product Repository
 * This is a domain interface - infrastructure will implement it
 */
public interface ProductRepository {
    
    Product save(Product product);
    
    Optional<Product> findById(Long id);
    
    List<Product> findAll();
    
    List<Product> search(String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}

