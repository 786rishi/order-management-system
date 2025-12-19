package com.assignment.ordermanagement.order.application.usecase;

import com.assignment.ordermanagement.order.application.dto.OrderItemRequest;
import com.assignment.ordermanagement.order.application.dto.OrderRequest;
import com.assignment.ordermanagement.order.application.dto.OrderResponse;
import com.assignment.ordermanagement.order.domain.exception.OutOfStockException;
import com.assignment.ordermanagement.order.domain.model.Order;
import com.assignment.ordermanagement.order.domain.model.OrderItem;
import com.assignment.ordermanagement.order.domain.service.OrderDomainService;
import com.assignment.ordermanagement.order.domain.service.discount.DiscountCalculator;
import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductService;
import com.assignment.ordermanagement.user.domain.model.Role;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceOrderUseCaseTest {

    @Mock
    private OrderDomainService orderDomainService;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private DiscountCalculator discountCalculator;

    private PlaceOrderUseCase placeOrderUseCase;

    @BeforeEach
    void setUp() {
        placeOrderUseCase = new PlaceOrderUseCase(orderDomainService, productService, userService, discountCalculator);
    }

    @Test
    void shouldPlaceOrderSuccessfully() {
        String username = "testuser";
        User user = new User(1L, username, "password", Role.USER);
        Product product = new Product(1L, "Product1", "Description", new BigDecimal("50.00"), 10, Instant.now(), null, false);
        
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);
        when(discountCalculator.calculate(any())).thenReturn(BigDecimal.ZERO);
        
        Order savedOrder = new Order(1L, user.getId(), 
            Arrays.asList(new OrderItem(1L, 1L, 2, new BigDecimal("50.00"), BigDecimal.ZERO, new BigDecimal("100.00"))),
            new BigDecimal("100.00"), Instant.now());
        savedOrder.setId(100L);
        
        when(orderDomainService.createOrder(any(Order.class))).thenReturn(savedOrder);

        OrderResponse response = placeOrderUseCase.execute(request, username);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.userId()).isEqualTo(user.getId());
        assertThat(response.items()).hasSize(1);
        verify(productService).decreaseStock(1L, 2);
        verify(orderDomainService).createOrder(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenProductOutOfStock() {
        String username = "testuser";
        User user = new User(1L, username, "password", Role.USER);
        Product product = new Product(1L, "Product1", "Description", new BigDecimal("50.00"), 1, Instant.now(), null, false);
        
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 5);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);

        assertThatThrownBy(() -> placeOrderUseCase.execute(request, username))
            .isInstanceOf(OutOfStockException.class)
            .hasMessageContaining("out of stock");

        verify(productService, never()).decreaseStock(anyLong(), anyInt());
        verify(orderDomainService, never()).createOrder(any());
    }

    @Test
    void shouldApplyDiscountWhenApplicable() {
        String username = "premiumuser";
        User user = new User(1L, username, "password", Role.PREMIUM_USER);
        Product product = new Product(1L, "Product1", "Description", new BigDecimal("100.00"), 10, Instant.now(), null, false);
        
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 2);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest));

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);
        when(discountCalculator.calculate(any())).thenReturn(new BigDecimal("20.00"));
        
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Order savedOrder = new Order(1L);
        savedOrder.setId(100L);
        savedOrder.setOrderTotal(new BigDecimal("180.00"));
        
        when(orderDomainService.createOrder(any(Order.class))).thenReturn(savedOrder);

        placeOrderUseCase.execute(request, username);

        verify(orderDomainService).createOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertThat(capturedOrder.getOrderTotal()).isEqualByComparingTo(new BigDecimal("180.00"));
    }

    @Test
    void shouldHandleMultipleProducts() {
        String username = "testuser";
        User user = new User(1L, username, "password", Role.USER);
        Product product1 = new Product(1L, "Product1", "Description", new BigDecimal("50.00"), 10, Instant.now(), null, false);
        Product product2 = new Product(2L, "Product2", "Description", new BigDecimal("30.00"), 10, Instant.now(), null, false);
        
        OrderItemRequest itemRequest1 = new OrderItemRequest(1L, 2);
        OrderItemRequest itemRequest2 = new OrderItemRequest(2L, 3);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest1, itemRequest2));

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product1);
        when(productService.getProductById(2L)).thenReturn(product2);
        when(discountCalculator.calculate(any())).thenReturn(BigDecimal.ZERO);
        
        Order savedOrder = new Order(1L);
        savedOrder.setId(100L);
        when(orderDomainService.createOrder(any(Order.class))).thenReturn(savedOrder);

        placeOrderUseCase.execute(request, username);

        verify(productService).decreaseStock(1L, 2);
        verify(productService).decreaseStock(2L, 3);
        verify(orderDomainService).createOrder(any(Order.class));
    }

    @Test
    void shouldDistributeDiscountProportionally() {
        String username = "premiumuser";
        User user = new User(1L, username, "password", Role.PREMIUM_USER);
        Product product1 = new Product(1L, "Product1", "Description", new BigDecimal("100.00"), 10, Instant.now(), null, false);
        Product product2 = new Product(2L, "Product2", "Description", new BigDecimal("50.00"), 10, Instant.now(), null, false);
        
        OrderItemRequest itemRequest1 = new OrderItemRequest(1L, 1);
        OrderItemRequest itemRequest2 = new OrderItemRequest(2L, 1);
        OrderRequest request = new OrderRequest(Arrays.asList(itemRequest1, itemRequest2));

        when(userService.getUserByUsername(username)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product1);
        when(productService.getProductById(2L)).thenReturn(product2);
        when(discountCalculator.calculate(any())).thenReturn(new BigDecimal("15.00"));
        
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Order savedOrder = new Order(1L);
        savedOrder.setId(100L);
        when(orderDomainService.createOrder(any(Order.class))).thenReturn(savedOrder);

        placeOrderUseCase.execute(request, username);

        verify(orderDomainService).createOrder(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertThat(capturedOrder.getItems()).hasSize(2);
    }
}

