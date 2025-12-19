package com.assignment.ordermanagement.order.adapter.rest;

import com.assignment.ordermanagement.order.application.dto.OrderRequest;
import com.assignment.ordermanagement.order.application.dto.OrderResponse;
import com.assignment.ordermanagement.order.application.usecase.PlaceOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Order operations
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    public OrderController(PlaceOrderUseCase placeOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
    }

    @PostMapping
    @Operation(summary = "Place a new order", security = @SecurityRequirement(name = "bearer-jwt"))
    @PreAuthorize("hasAnyRole('USER','PREMIUM_USER')")
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        OrderResponse response = placeOrderUseCase.execute(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

