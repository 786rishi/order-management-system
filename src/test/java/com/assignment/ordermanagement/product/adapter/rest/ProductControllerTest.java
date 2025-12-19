package com.assignment.ordermanagement.product.adapter.rest;

import com.assignment.ordermanagement.product.application.dto.ProductRequest;
import com.assignment.ordermanagement.product.application.dto.ProductResponse;
import com.assignment.ordermanagement.product.application.usecase.*;
import com.assignment.ordermanagement.product.domain.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationThatNeedsToBeAtLeast256BitsLong12345678",
    "jwt.expiration=3600000"
})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @MockBean
    private GetAllProductsUseCase getAllProductsUseCase;

    @MockBean
    private SearchProductsUseCase searchProductsUseCase;

    @MockBean
    private UpdateProductUseCase updateProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Product", "Description", new BigDecimal("10.00"), 5);
        ProductResponse response = new ProductResponse(1L, "Product", "Description", new BigDecimal("10.00"), 5, true);

        when(createProductUseCase.execute(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Product"))
            .andExpect(jsonPath("$.price").value(10.00));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void shouldReturn403WhenNonAdminCreatesProduct() throws Exception {
        ProductRequest request = new ProductRequest("Product", "Description", new BigDecimal("10.00"), 5);

        mockMvc.perform(post("/api/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetAllProductsWithoutAuthentication() throws Exception {
        ProductResponse response1 = new ProductResponse(1L, "Product1", "Description1", new BigDecimal("10.00"), 5, true);
        ProductResponse response2 = new ProductResponse(2L, "Product2", "Description2", new BigDecimal("20.00"), 10, true);

        when(getAllProductsUseCase.execute()).thenReturn(Arrays.asList(response1, response2));

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() throws Exception {
        when(getAllProductsUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldSearchProductsByName() throws Exception {
        ProductResponse response = new ProductResponse(1L, "Test Product", "Description", new BigDecimal("10.00"), 5, true);

        when(searchProductsUseCase.execute(eq("Test"), eq(null), eq(null), eq(null)))
            .thenReturn(Arrays.asList(response));

        mockMvc.perform(get("/api/products/search")
                .param("name", "Test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].name").value("Test Product"));
    }

    @Test
    void shouldSearchProductsByPriceRange() throws Exception {
        ProductResponse response = new ProductResponse(1L, "Product", "Description", new BigDecimal("25.00"), 5, true);

        when(searchProductsUseCase.execute(eq(null), eq(new BigDecimal("10.00")), eq(new BigDecimal("50.00")), eq(null)))
            .thenReturn(Arrays.asList(response));

        mockMvc.perform(get("/api/products/search")
                .param("minPrice", "10.00")
                .param("maxPrice", "50.00"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].price").value(25.00));
    }

    @Test
    void shouldSearchInStockProducts() throws Exception {
        ProductResponse response = new ProductResponse(1L, "Product", "Description", new BigDecimal("10.00"), 5, true);

        when(searchProductsUseCase.execute(eq(null), eq(null), eq(null), eq(true)))
            .thenReturn(Arrays.asList(response));

        mockMvc.perform(get("/api/products/search")
                .param("inStock", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].inStock").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldUpdateProduct() throws Exception {
        Long id = 1L;
        ProductRequest request = new ProductRequest("Updated Product", "Updated Description", new BigDecimal("20.00"), 10);
        ProductResponse response = new ProductResponse(id, "Updated Product", "Updated Description", new BigDecimal("20.00"), 10, true);

        when(updateProductUseCase.execute(eq(id), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/products/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Product"))
            .andExpect(jsonPath("$.price").value(20.00));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturn404WhenUpdatingNonExistentProduct() throws Exception {
        Long id = 999L;
        ProductRequest request = new ProductRequest("Product", "Description", new BigDecimal("10.00"), 5);

        when(updateProductUseCase.execute(eq(id), any(ProductRequest.class)))
            .thenThrow(new ProductNotFoundException("Product not found with id: " + id));

        mockMvc.perform(put("/api/products/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldDeleteProduct() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/products/{id}", id)
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturn404WhenDeletingNonExistentProduct() throws Exception {
        Long id = 999L;

        doThrow(new ProductNotFoundException("Product not found with id: " + id))
            .when(deleteProductUseCase).execute(id);

        mockMvc.perform(delete("/api/products/{id}", id)
                .with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void shouldReturn403WhenNonAdminDeletesProduct() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 1L)
                .with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturn400WhenCreateProductWithInvalidData() throws Exception {
        ProductRequest request = new ProductRequest("", "Description", new BigDecimal("-10.00"), -5);

        mockMvc.perform(post("/api/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}

