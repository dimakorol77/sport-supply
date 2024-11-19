package org.example.mappers;

import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    // Преобразование сущности OrderItem в DTO OrderItemDto
    public OrderItemDto toDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem);
    }

    // Преобразование DTO OrderItemCreateDto и Product в сущность OrderItem
    public OrderItem toEntity(OrderItemCreateDto orderItemCreateDto, Product product) {
        if (orderItemCreateDto == null || product == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
        orderItem.setProductCategoryName(product.getCategory().getName());
        orderItem.setPrice(orderItemCreateDto.getPrice());
        orderItem.setQuantity(orderItemCreateDto.getQuantity());
        return orderItem;
    }

    // Обновление сущности OrderItem на основе данных из DTO OrderItemCreateDto и Product
    public void updateEntityFromCreateDto(OrderItemCreateDto orderItemCreateDto, OrderItem orderItem, Product product) {
        if (orderItemCreateDto == null || orderItem == null || product == null) {
            return;
        }
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
        orderItem.setProductCategoryName(product.getCategory().getName());
        orderItem.setPrice(orderItemCreateDto.getPrice());
        orderItem.setQuantity(orderItemCreateDto.getQuantity());
    }

}
