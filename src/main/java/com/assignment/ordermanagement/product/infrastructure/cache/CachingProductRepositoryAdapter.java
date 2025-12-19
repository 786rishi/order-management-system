package com.assignment.ordermanagement.product.infrastructure.cache;

import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Caching decorator for ProductRepository
 * Adds caching capabilities to the repository
 */
public class CachingProductRepositoryAdapter implements ProductRepository {

    private final ProductRepository delegate;

    public CachingProductRepositoryAdapter(ProductRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    @CacheEvict(value = {"products", "productList"}, allEntries = true)
    public Product save(Product product) {
        return delegate.save(product);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> findById(Long id) {
        return delegate.findById(id);
    }

    @Override
    @Cacheable(value = "productList")
    public List<Product> findAll() {
        return delegate.findAll();
    }

    @Override
    public List<Product> search(String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        return delegate.search(name, minPrice, maxPrice, inStock);
    }

    @Override
    @CacheEvict(value = {"products", "productList"}, allEntries = true)
    public void deleteById(Long id) {
        delegate.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return delegate.existsById(id);
    }
}

