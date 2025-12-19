package com.assignment.ordermanagement.product.infrastructure.persistence.repository;

import com.assignment.ordermanagement.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA Repository for ProductEntity
 */
public interface ProductRepositoryJpa extends JpaRepository<ProductEntity, Long>, 
                                               JpaSpecificationExecutor<ProductEntity> {
}

