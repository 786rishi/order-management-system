package com.assignment.ordermanagement.order.application.usecase;

import com.assignment.ordermanagement.order.application.dto.OrderItemRequest;
import com.assignment.ordermanagement.order.application.dto.OrderItemResponse;
import com.assignment.ordermanagement.order.application.dto.OrderRequest;
import com.assignment.ordermanagement.order.application.dto.OrderResponse;
import com.assignment.ordermanagement.order.domain.exception.OutOfStockException;
import com.assignment.ordermanagement.order.domain.model.Order;
import com.assignment.ordermanagement.order.domain.model.OrderContext;
import com.assignment.ordermanagement.order.domain.model.OrderItem;
import com.assignment.ordermanagement.order.domain.service.OrderDomainService;
import com.assignment.ordermanagement.order.domain.service.discount.DiscountCalculator;
import com.assignment.ordermanagement.product.domain.model.Product;
import com.assignment.ordermanagement.product.domain.port.ProductService;
import com.assignment.ordermanagement.user.domain.model.User;
import com.assignment.ordermanagement.user.domain.port.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case: Place a new order
 * Orchestrates: Product validation, stock checking, discount calculation, order creation
 */
public class PlaceOrderUseCase {

    private final OrderDomainService orderDomainService;
    private final ProductService productService;
    private final UserService userService;
    private final DiscountCalculator discountCalculator;

    public PlaceOrderUseCase(OrderDomainService orderDomainService,
                           ProductService productService,
                           UserService userService,
                           DiscountCalculator discountCalculator) {
        this.orderDomainService = orderDomainService;
        this.productService = productService;
        this.userService = userService;
        this.discountCalculator = discountCalculator;
    }

    public OrderResponse execute(OrderRequest request, String username) {
        // Get user
        User user = userService.getUserByUsername(username);
        
        // Create order
        Order order = new Order(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        // Process each order item
        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productService.getProductById(itemRequest.productId());
            
            // Check stock
            if (!product.hasStock(itemRequest.quantity())) {
                throw new OutOfStockException(
                    "Product " + product.getName() + " is out of stock. Available: " + product.getQuantity()
                );
            }

            // Decrease stock
            productService.decreaseStock(product.getId(), itemRequest.quantity());

            // Create order item
            OrderItem orderItem = new OrderItem(
                product.getId(),
                itemRequest.quantity(),
                product.getPrice()
            );
            
            orderItems.add(orderItem);
            subtotal = subtotal.add(orderItem.getTotalPrice());
        }

        // Calculate discount
        OrderContext context = new OrderContext(user.getRole(), subtotal);
        BigDecimal totalDiscount = discountCalculator.calculate(context);

        // Distribute discount across items proportionally
        if (totalDiscount.compareTo(BigDecimal.ZERO) > 0 && subtotal.compareTo(BigDecimal.ZERO) > 0) {
            distributeDiscount(orderItems, totalDiscount, subtotal);
        }

        // Add items to order
        order.addItems(orderItems);
        order.setOrderTotal(subtotal.subtract(totalDiscount));

        // Save order
        Order savedOrder = orderDomainService.createOrder(order);

        return mapToResponse(savedOrder);
    }

    private void distributeDiscount(List<OrderItem> items, BigDecimal totalDiscount, BigDecimal subtotal) {
        BigDecimal remainingDiscount = totalDiscount;
        
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            BigDecimal itemDiscount;
            
            if (i == items.size() - 1) {
                // Last item gets remaining discount to avoid rounding issues
                itemDiscount = remainingDiscount;
            } else {
                // Proportional discount
                itemDiscount = item.getTotalPrice()
                    .multiply(totalDiscount)
                    .divide(subtotal, 2, RoundingMode.HALF_UP);
                remainingDiscount = remainingDiscount.subtract(itemDiscount);
            }
            
            item.applyDiscount(itemDiscount);
        }
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
            .map(item -> new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getDiscountApplied(),
                item.getTotalPrice()
            ))
            .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            itemResponses,
            order.getOrderTotal(),
            order.getCreatedAt()
        );
    }
}

