package com.assignment.ordermanagement.product.infrastructure.cache;

import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachingProductRepositoryAdapterTest {

    @Mock
    private ProductRepository delegate;

    private CachingProductRepositoryAdapter cachingAdapter;

    @BeforeEach
    void setUp() {
        cachingAdapter = new CachingProductRepositoryAdapter(delegate);
    }

    @Test
    void shouldDelegateSave() {
        Product product = new Product("Product", "Description", new BigDecimal("10.00"), 5);
        Product savedProduct = new Product(1L, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);

        when(delegate.save(product)).thenReturn(savedProduct);

        Product result = cachingAdapter.save(product);

        assertThat(result).isEqualTo(savedProduct);
        verify(delegate).save(product);
    }

    @Test
    void shouldDelegateFindById() {
        Long id = 1L;
        Product product = new Product(id, "Product", "Description", new BigDecimal("10.00"), 5, Instant.now(), null, false);

        when(delegate.findById(id)).thenReturn(Optional.of(product));

        Optional<Product> result = cachingAdapter.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product);
        verify(delegate).findById(id);
    }

    @Test
    void shouldDelegateFindAll() {
        List<Product> products = Arrays.asList(
            new Product(1L, "Product1", "Description1", new BigDecimal("10.00"), 5, Instant.now(), null, false),
            new Product(2L, "Product2", "Description2", new BigDecimal("20.00"), 10, Instant.now(), null, false)
        );

        when(delegate.findAll()).thenReturn(products);

        List<Product> result = cachingAdapter.findAll();

        assertThat(result).hasSize(2);
        verify(delegate).findAll();
    }

    @Test
    void shouldDelegateSearch() {
        String name = "Test";
        BigDecimal minPrice = new BigDecimal("10.00");
        BigDecimal maxPrice = new BigDecimal("50.00");
        Boolean inStock = true;
        List<Product> products = Arrays.asList(
            new Product(1L, "Test Product", "Description", new BigDecimal("25.00"), 5, Instant.now(), null, false)
        );

        when(delegate.search(name, minPrice, maxPrice, inStock)).thenReturn(products);

        List<Product> result = cachingAdapter.search(name, minPrice, maxPrice, inStock);

        assertThat(result).hasSize(1);
        verify(delegate).search(name, minPrice, maxPrice, inStock);
    }

    @Test
    void shouldDelegateDeleteById() {
        Long id = 1L;

        cachingAdapter.deleteById(id);

        verify(delegate).deleteById(id);
    }

    @Test
    void shouldDelegateExistsById() {
        Long id = 1L;

        when(delegate.existsById(id)).thenReturn(true);

        boolean result = cachingAdapter.existsById(id);

        assertThat(result).isTrue();
        verify(delegate).existsById(id);
    }
}

