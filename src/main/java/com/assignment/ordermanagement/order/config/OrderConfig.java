package com.assignment.ordermanagement.order.config;

import com.assignment.ordermanagement.order.application.usecase.PlaceOrderUseCase;
import com.assignment.ordermanagement.order.domain.port.OrderRepository;
import com.assignment.ordermanagement.order.domain.service.OrderDomainService;
import com.assignment.ordermanagement.order.domain.service.discount.DiscountCalculator;
import com.assignment.ordermanagement.order.domain.service.discount.DiscountStrategy;
import com.assignment.ordermanagement.order.domain.service.discount.HighValueOrderDiscount;
import com.assignment.ordermanagement.order.domain.service.discount.PremiumUserDiscount;
import com.assignment.ordermanagement.order.infrastructure.persistence.adapter.OrderRepositoryAdapter;
import com.assignment.ordermanagement.order.infrastructure.persistence.mapper.OrderEntityMapper;
import com.assignment.ordermanagement.order.infrastructure.persistence.repository.OrderRepositoryJpa;
import com.assignment.ordermanagement.product.domain.port.ProductService;
import com.assignment.ordermanagement.user.domain.port.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for Order feature
 * Wires all layers together following dependency inversion
 */
@Configuration
public class OrderConfig {

    // Infrastructure Layer
    @Bean
    public OrderEntityMapper orderEntityMapper() {
        return new OrderEntityMapper();
    }

    @Bean
    public OrderRepository orderRepository(OrderRepositoryJpa jpaRepository, OrderEntityMapper mapper) {
        return new OrderRepositoryAdapter(jpaRepository, mapper);
    }

    // Domain Layer - Discount Strategies
    @Bean
    public DiscountStrategy premiumUserDiscount() {
        return new PremiumUserDiscount();
    }

    @Bean
    public DiscountStrategy highValueOrderDiscount() {
        return new HighValueOrderDiscount();
    }

    @Bean
    public DiscountCalculator discountCalculator(List<DiscountStrategy> strategies) {
        return new DiscountCalculator(strategies);
    }

    // Domain Layer - Service
    @Bean
    public OrderDomainService orderDomainService(OrderRepository orderRepository) {
        return new OrderDomainService(orderRepository);
    }

    // Application Layer - Use Cases
    @Bean
    public PlaceOrderUseCase placeOrderUseCase(
            OrderDomainService orderDomainService,
            ProductService productService,
            UserService userService,
            DiscountCalculator discountCalculator) {
        return new PlaceOrderUseCase(orderDomainService, productService, userService, discountCalculator);
    }

}

