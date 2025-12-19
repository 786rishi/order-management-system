package com.assignment.ordermanagement.order.adapter.rest;

import com.assignment.ordermanagement.order.application.dto.OrderItemRequest;
import com.assignment.ordermanagement.order.application.dto.OrderItemResponse;
import com.assignment.ordermanagement.order.application.dto.OrderRequest;
import com.assignment.ordermanagement.order.application.dto.OrderResponse;
import com.assignment.ordermanagement.order.application.usecase.PlaceOrderUseCase;
import com.assignment.ordermanagement.order.domain.exception.OutOfStockException;
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
import java.time.Instant;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.secret=mySecretKeyForJWTTokenGenerationAndValidationThatNeedsToBeAtLeast256BitsLong12345678",
    "jwt.expiration=3600000"
})
class OrderControllerTest {

    private static final String API_ORDERS_URL = "/api/orders";
    private static final String TEST_USER = "testuser";
    private static final String PREMIUM_USER = "premiumuser";
    private static final String OUT_OF_STOCK_MESSAGE = "Product is out of stock";
    private static final String ROLE_USER = "USER";
    private static final String ROLE_PREMIUM_USER = "PREMIUM_USER";
    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long USER_ID_1 = 1L;
    private static final int QUANTITY_ONE = 1;
    private static final int QUANTITY_TWO = 2;
    private static final int QUANTITY_HUNDRED = 100;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlaceOrderUseCase placeOrderUseCase;

    @Test
    @WithMockUser(username = TEST_USER, roles = ROLE_USER)
    void shouldPlaceOrderSuccessfully() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(PRODUCT_ID_1, QUANTITY_TWO);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));
        
        OrderItemResponse itemResponse = new OrderItemResponse(PRODUCT_ID_1, PRODUCT_ID_1, QUANTITY_TWO, 
            new BigDecimal("50.00"), BigDecimal.ZERO, new BigDecimal("100.00"));
        OrderResponse response = new OrderResponse(PRODUCT_ID_1, USER_ID_1, 
            Arrays.asList(itemResponse), new BigDecimal("100.00"), Instant.now());

        when(placeOrderUseCase.execute(any(OrderRequest.class), eq(TEST_USER))).thenReturn(response);

        mockMvc.perform(post(API_ORDERS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.orderTotal").value(100.00));
    }

    @Test
    @WithMockUser(username = PREMIUM_USER, roles = ROLE_PREMIUM_USER)
    void shouldPlaceOrderAsPremiumUser() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(PRODUCT_ID_1, QUANTITY_ONE);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));
        
        OrderItemResponse itemResponse = new OrderItemResponse(PRODUCT_ID_1, PRODUCT_ID_1, QUANTITY_ONE, 
            new BigDecimal("100.00"), new BigDecimal("10.00"), new BigDecimal("100.00"));
        OrderResponse response = new OrderResponse(PRODUCT_ID_1, USER_ID_1, 
            Arrays.asList(itemResponse), new BigDecimal("90.00"), Instant.now());

        when(placeOrderUseCase.execute(any(OrderRequest.class), eq(PREMIUM_USER))).thenReturn(response);

        mockMvc.perform(post(API_ORDERS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderTotal").value(90.00));
    }

    @Test
    void shouldReturn403WhenNotAuthenticated() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(PRODUCT_ID_1, QUANTITY_ONE);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));

        mockMvc.perform(post(API_ORDERS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TEST_USER, roles = ROLE_USER)
    void shouldReturn400WhenOutOfStock() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(PRODUCT_ID_1, QUANTITY_HUNDRED);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));

        when(placeOrderUseCase.execute(any(OrderRequest.class), eq(TEST_USER)))
            .thenThrow(new OutOfStockException(OUT_OF_STOCK_MESSAGE));

        mockMvc.perform(post(API_ORDERS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = TEST_USER, roles = ROLE_USER)
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        OrderRequest request = new OrderRequest(Arrays.asList());

        mockMvc.perform(post(API_ORDERS_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}

