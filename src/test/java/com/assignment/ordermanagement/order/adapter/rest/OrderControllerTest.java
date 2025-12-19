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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlaceOrderUseCase placeOrderUseCase;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldPlaceOrderSuccessfully() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));
        
        OrderItemResponse itemResponse = new OrderItemResponse(1L, 1L, 2, 
            new BigDecimal("50.00"), BigDecimal.ZERO, new BigDecimal("100.00"));
        OrderResponse response = new OrderResponse(1L, 1L, 
            Arrays.asList(itemResponse), new BigDecimal("100.00"), Instant.now());

        when(placeOrderUseCase.execute(any(OrderRequest.class), eq("testuser"))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
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
    @WithMockUser(username = "premiumuser", roles = "PREMIUM_USER")
    void shouldPlaceOrderAsPremiumUser() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));
        
        OrderItemResponse itemResponse = new OrderItemResponse(1L, 1L, 1, 
            new BigDecimal("100.00"), new BigDecimal("10.00"), new BigDecimal("100.00"));
        OrderResponse response = new OrderResponse(1L, 1L, 
            Arrays.asList(itemResponse), new BigDecimal("90.00"), Instant.now());

        when(placeOrderUseCase.execute(any(OrderRequest.class), eq("premiumuser"))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderTotal").value(90.00));
    }

    @Test
    void shouldReturn403WhenNotAuthenticated() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 1);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));

        mockMvc.perform(post("/api/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldReturn400WhenOutOfStock() throws Exception {
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 100);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));

        when(placeOrderUseCase.execute(any(OrderRequest.class), eq("testuser")))
            .thenThrow(new OutOfStockException("Product is out of stock"));

        mockMvc.perform(post("/api/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        OrderRequest request = new OrderRequest(Arrays.asList());

        mockMvc.perform(post("/api/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}

