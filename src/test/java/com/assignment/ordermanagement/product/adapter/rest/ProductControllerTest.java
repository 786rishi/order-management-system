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

    private static final String API_PRODUCTS_URL = "/api/products";
    private static final String API_PRODUCTS_SEARCH_URL = "/api/products/search";
    private static final String ADMIN_USER = "admin";
    private static final String NORMAL_USER = "user";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private static final String PRODUCT_NAME = "Product";
    private static final String PRODUCT_DESCRIPTION = "Description";
    private static final String PRODUCT_NAME_1 = "Product1";
    private static final String PRODUCT_NAME_2 = "Product2";
    private static final String DESCRIPTION_1 = "Description1";
    private static final String DESCRIPTION_2 = "Description2";
    private static final String TEST_PRODUCT_NAME = "Test Product";
    private static final String UPDATED_PRODUCT_NAME = "Updated Product";
    private static final String UPDATED_DESCRIPTION = "Updated Description";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_MIN_PRICE = "minPrice";
    private static final String PARAM_MAX_PRICE = "maxPrice";
    private static final String PARAM_IN_STOCK = "inStock";
    private static final String TEST_SEARCH_TERM = "Test";
    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long PRODUCT_ID_2 = 2L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final int STOCK_QUANTITY_5 = 5;
    private static final int STOCK_QUANTITY_10 = 10;

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
    @WithMockUser(username = ADMIN_USER, roles = ROLE_ADMIN)
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest(PRODUCT_NAME, PRODUCT_DESCRIPTION, new BigDecimal("10.00"), STOCK_QUANTITY_5);
        ProductResponse response = new ProductResponse(PRODUCT_ID_1, PRODUCT_NAME, PRODUCT_DESCRIPTION, new BigDecimal("10.00"), STOCK_QUANTITY_5, true);

        when(createProductUseCase.execute(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post(API_PRODUCTS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
            .andExpect(jsonPath("$.price").value(10.00));
    }

    @Test
    @WithMockUser(username = NORMAL_USER, roles = ROLE_USER)
    void shouldReturn403WhenNonAdminCreatesProduct() throws Exception {
        ProductRequest request = new ProductRequest(PRODUCT_NAME, PRODUCT_DESCRIPTION, new BigDecimal("10.00"), STOCK_QUANTITY_5);

        mockMvc.perform(post(API_PRODUCTS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetAllProductsWithoutAuthentication() throws Exception {
        ProductResponse response1 = new ProductResponse(PRODUCT_ID_1, PRODUCT_NAME_1, DESCRIPTION_1, new BigDecimal("10.00"), STOCK_QUANTITY_5, true);
        ProductResponse response2 = new ProductResponse(PRODUCT_ID_2, PRODUCT_NAME_2, DESCRIPTION_2, new BigDecimal("20.00"), STOCK_QUANTITY_10, true);

        when(getAllProductsUseCase.execute()).thenReturn(Arrays.asList(response1, response2));

        mockMvc.perform(get(API_PRODUCTS_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() throws Exception {
        when(getAllProductsUseCase.execute()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(API_PRODUCTS_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldSearchProductsByName() throws Exception {
        ProductResponse response = new ProductResponse(PRODUCT_ID_1, TEST_PRODUCT_NAME, PRODUCT_DESCRIPTION, new BigDecimal("10.00"), STOCK_QUANTITY_5, true);

        when(searchProductsUseCase.execute(eq(TEST_SEARCH_TERM), eq(null), eq(null), eq(null)))
            .thenReturn(Arrays.asList(response));

        mockMvc.perform(get(API_PRODUCTS_SEARCH_URL)
                .param(PARAM_NAME, TEST_SEARCH_TERM))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].name").value(TEST_PRODUCT_NAME));
    }

    @Test
    void shouldSearchProductsByPriceRange() throws Exception {
        ProductResponse response = new ProductResponse(PRODUCT_ID_1, PRODUCT_NAME, PRODUCT_DESCRIPTION, new BigDecimal("25.00"), STOCK_QUANTITY_5, true);

        when(searchProductsUseCase.execute(eq(null), eq(new BigDecimal("10.00")), eq(new BigDecimal("50.00")), eq(null)))
            .thenReturn(Arrays.asList(response));

        mockMvc.perform(get(API_PRODUCTS_SEARCH_URL)
                .param(PARAM_MIN_PRICE, "10.00")
                .param(PARAM_MAX_PRICE, "50.00"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].price").value(25.00));
    }

    @Test
    void shouldSearchInStockProducts() throws Exception {
        ProductResponse response = new ProductResponse(PRODUCT_ID_1, PRODUCT_NAME, PRODUCT_DESCRIPTION, new BigDecimal("10.00"), STOCK_QUANTITY_5, true);

        when(searchProductsUseCase.execute(eq(null), eq(null), eq(null), eq(true)))
            .thenReturn(Arrays.asList(response));

        mockMvc.perform(get(API_PRODUCTS_SEARCH_URL)
                .param(PARAM_IN_STOCK, "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].inStock").value(true));
    }

    @Test
    @WithMockUser(username = ADMIN_USER, roles = ROLE_ADMIN)
    void shouldUpdateProduct() throws Exception {
        ProductRequest request = new ProductRequest(UPDATED_PRODUCT_NAME, UPDATED_DESCRIPTION, new BigDecimal("20.00"), STOCK_QUANTITY_10);
        ProductResponse response = new ProductResponse(PRODUCT_ID_1, UPDATED_PRODUCT_NAME, UPDATED_DESCRIPTION, new BigDecimal("20.00"), STOCK_QUANTITY_10, true);

        when(updateProductUseCase.execute(eq(PRODUCT_ID_1), any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(put(API_PRODUCTS_URL + "/{id}", PRODUCT_ID_1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(UPDATED_PRODUCT_NAME))
            .andExpect(jsonPath("$.price").value(20.00));
    }

    @Test
    @WithMockUser(username = ADMIN_USER, roles = ROLE_ADMIN)
    void shouldReturn404WhenUpdatingNonExistentProduct() throws Exception {
        ProductRequest request = new ProductRequest(PRODUCT_NAME, PRODUCT_DESCRIPTION, new BigDecimal("10.00"), STOCK_QUANTITY_5);

        when(updateProductUseCase.execute(eq(NON_EXISTENT_ID), any(ProductRequest.class)))
            .thenThrow(new ProductNotFoundException("Product not found with id: " + NON_EXISTENT_ID));

        mockMvc.perform(put(API_PRODUCTS_URL + "/{id}", NON_EXISTENT_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = ADMIN_USER, roles = ROLE_ADMIN)
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete(API_PRODUCTS_URL + "/{id}", PRODUCT_ID_1)
                .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = ADMIN_USER, roles = ROLE_ADMIN)
    void shouldReturn404WhenDeletingNonExistentProduct() throws Exception {
        doThrow(new ProductNotFoundException("Product not found with id: " + NON_EXISTENT_ID))
            .when(deleteProductUseCase).execute(NON_EXISTENT_ID);

        mockMvc.perform(delete(API_PRODUCTS_URL + "/{id}", NON_EXISTENT_ID)
                .with(csrf()))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = NORMAL_USER, roles = ROLE_USER)
    void shouldReturn403WhenNonAdminDeletesProduct() throws Exception {
        mockMvc.perform(delete(API_PRODUCTS_URL + "/{id}", PRODUCT_ID_1)
                .with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = ADMIN_USER, roles = ROLE_ADMIN)
    void shouldReturn400WhenCreateProductWithInvalidData() throws Exception {
        ProductRequest request = new ProductRequest("", PRODUCT_DESCRIPTION, new BigDecimal("-10.00"), -5);

        mockMvc.perform(post(API_PRODUCTS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}

