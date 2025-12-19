package com.assignment.ordermanagement.product.infrastructure.persistence.adapter;

import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductRepository;
import com.assignment.ordermanagement.product.infrastructure.persistence.entity.ProductEntity;
import com.assignment.ordermanagement.product.infrastructure.persistence.mapper.ProductEntityMapper;
import com.assignment.ordermanagement.product.infrastructure.persistence.repository.ProductRepositoryJpa;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the domain ProductRepository port using JPA
 */
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductRepositoryJpa jpaRepository;
    private final ProductEntityMapper mapper;

    public ProductRepositoryAdapter(ProductRepositoryJpa jpaRepository, ProductEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> search(String name, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock) {
        Specification<ProductEntity> spec = Specification.where(notDeleted());

        if (name != null && !name.isBlank()) {
            spec = spec.and(nameContains(name));
        }
        if (minPrice != null) {
            spec = spec.and(priceGreaterThanOrEqual(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(priceLessThanOrEqual(maxPrice));
        }
        if (inStock != null && inStock) {
            spec = spec.and(inStockSpec());
        }

        return jpaRepository.findAll(spec).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    // Specifications for search criteria
    private Specification<ProductEntity> notDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    private Specification<ProductEntity> nameContains(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private Specification<ProductEntity> priceGreaterThanOrEqual(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private Specification<ProductEntity> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    private Specification<ProductEntity> inStockSpec() {
        return (root, query, cb) -> cb.greaterThan(root.get("quantity"), 0);
    }
}

