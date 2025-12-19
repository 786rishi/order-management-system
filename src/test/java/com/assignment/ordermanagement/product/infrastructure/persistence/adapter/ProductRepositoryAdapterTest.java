package com.assignment.ordermanagement.product.infrastructure.persistence.adapter;

import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.infrastructure.persistence.entity.ProductEntity;
import com.assignment.ordermanagement.product.infrastructure.persistence.mapper.ProductEntityMapper;
import com.assignment.ordermanagement.product.infrastructure.persistence.repository.ProductRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private ProductRepositoryJpa jpaRepository;

    @Mock
    private ProductEntityMapper mapper;

    private ProductRepositoryAdapter productRepositoryAdapter;

    @BeforeEach
    void setUp() {
        productRepositoryAdapter = new ProductRepositoryAdapter(jpaRepository, mapper);
    }

    @Test
    void shouldSaveProduct() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 5);
        ProductEntity entity = new ProductEntity();
        ProductEntity savedEntity = new ProductEntity();
        savedEntity.setId(1L);
        Product savedProduct = new Product(1L, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);

        when(mapper.toEntity(product)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(savedProduct);

        Product result = productRepositoryAdapter.save(product);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(mapper).toEntity(product);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void shouldFindProductById() {
        Long id = 1L;
        ProductEntity entity = new ProductEntity();
        entity.setId(id);
        Product product = new Product(id, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(product);

        Optional<Product> result = productRepositoryAdapter.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        verify(jpaRepository).findById(id);
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        Long id = 999L;

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Product> result = productRepositoryAdapter.findById(id);

        assertThat(result).isEmpty();
        verify(jpaRepository).findById(id);
    }

    @Test
    void shouldFindAllProducts() {
        ProductEntity entity1 = new ProductEntity();
        entity1.setId(1L);
        ProductEntity entity2 = new ProductEntity();
        entity2.setId(2L);
        Product product1 = new Product(1L, "Product1", "Description1", new BigDecimal("10.00"), 5, Instant.now(), null, false);
        Product product2 = new Product(2L, "Product2", "Description2", new BigDecimal("20.00"), 10, Instant.now(), null, false);

        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(mapper.toDomain(entity1)).thenReturn(product1);
        when(mapper.toDomain(entity2)).thenReturn(product2);

        List<Product> result = productRepositoryAdapter.findAll();

        assertThat(result).hasSize(2);
        verify(jpaRepository).findAll();
    }

    @Test
    void shouldSearchProducts() {
        String name = "Test";
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");
        Boolean inStock = true;

        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        Product product = new Product(1L, "Test Product", "Description", new BigDecimal("25.00"), 5, Instant.now(), null, false);

        when(jpaRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(entity));
        when(mapper.toDomain(entity)).thenReturn(product);

        List<Product> result = productRepositoryAdapter.search(name, minPrice, maxPrice, inStock);

        assertThat(result).hasSize(1);
        verify(jpaRepository).findAll(any(Specification.class));
    }

    @Test
    void shouldSearchWithNullCriteria() {
        ProductEntity entity = new ProductEntity();
        Product product = new Product(1L, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);

        when(jpaRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(entity));
        when(mapper.toDomain(entity)).thenReturn(product);

        List<Product> result = productRepositoryAdapter.search(null, null, null, null);

        assertThat(result).hasSize(1);
        verify(jpaRepository).findAll(any(Specification.class));
    }

    @Test
    void shouldDeleteProductById() {
        Long id = 1L;

        productRepositoryAdapter.deleteById(id);

        verify(jpaRepository).deleteById(id);
    }

    @Test
    void shouldCheckIfProductExists() {
        Long id = 1L;

        when(jpaRepository.existsById(id)).thenReturn(true);

        boolean result = productRepositoryAdapter.existsById(id);

        assertThat(result).isTrue();
        verify(jpaRepository).existsById(id);
    }

    @Test
    void shouldReturnFalseWhenProductDoesNotExist() {
        Long id = 999L;

        when(jpaRepository.existsById(id)).thenReturn(false);

        boolean result = productRepositoryAdapter.existsById(id);

        assertThat(result).isFalse();
        verify(jpaRepository).existsById(id);
    }
}

