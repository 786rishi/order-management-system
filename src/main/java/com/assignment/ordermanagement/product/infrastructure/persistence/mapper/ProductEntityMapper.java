package com.assignment.ordermanagement.product.infrastructure.persistence.mapper;

import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.infrastructure.persistence.entity.ProductEntity;

/**
 * Mapper between Domain Model and JPA Entity
 */
public class ProductEntityMapper {

    public ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setQuantity(product.getQuantity());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());
        entity.setDeleted(product.isDeleted());
        
        return entity;
    }

    public Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isDeleted()
        );
    }
}

