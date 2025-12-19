package com.assignment.ordermanagement.order.infrastructure.persistence.mapper;

import com.assignment.ordermanagement.order.domain.model.Order;
import com.assignment.ordermanagement.order.domain.model.OrderItem;
import com.assignment.ordermanagement.order.infrastructure.persistence.entity.OrderEntity;
import com.assignment.ordermanagement.order.infrastructure.persistence.entity.OrderItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper between Order Domain Model and JPA Entity
 */
public class OrderEntityMapper {

    public OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUserId(order.getUserId());
        entity.setOrderTotal(order.getOrderTotal());
        entity.setCreatedAt(order.getCreatedAt());

        // Map items
        List<OrderItemEntity> itemEntities = order.getItems().stream()
            .map(item -> toItemEntity(item, entity))
            .collect(Collectors.toList());
        entity.setItems(itemEntities);

        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        // Map items
        List<OrderItem> items = entity.getItems() != null 
            ? entity.getItems().stream()
                .map(this::toItemDomain)
                .collect(Collectors.toList())
            : new ArrayList<>();

        return new Order(
            entity.getId(),
            entity.getUserId(),
            items,
            entity.getOrderTotal(),
            entity.getCreatedAt()
        );
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity orderEntity) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(item.getId());
        entity.setOrder(orderEntity);
        entity.setProductId(item.getProductId());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setDiscountApplied(item.getDiscountApplied());
        entity.setTotalPrice(item.getTotalPrice());
        return entity;
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {
        return new OrderItem(
            entity.getId(),
            entity.getProductId(),
            entity.getQuantity(),
            entity.getUnitPrice(),
            entity.getDiscountApplied(),
            entity.getTotalPrice()
        );
    }
}

