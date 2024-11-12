package org.example.mappers;

import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    // Преобразование сущности OrderItem в DTO OrderItemDto
    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        // Используем конструктор с параметром для создания DTO
        return new OrderItemDto(orderItem);
    }

    // Преобразование DTO OrderItemDto в сущность OrderItem
    public OrderItem toEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
        return orderItem;
    }

    // Обновление сущности OrderItem на основе данных из DTO OrderItemDto
    public void updateEntityFromDto(OrderItemDto orderItemDto, OrderItem orderItem) {
        if (orderItemDto == null || orderItem == null) {
            return;
        }
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
    }
}
