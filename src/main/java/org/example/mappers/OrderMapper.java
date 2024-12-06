package org.example.mappers;

import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.dto.OrderItemDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.models.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

public OrderDto toDto(Order order) {
    if (order == null) {
        return null;
    }

    OrderDto dto = new OrderDto();
    dto.setId(order.getId());
    dto.setTotalAmount(order.getTotalAmount());
    dto.setDeliveryMethod(order.getDeliveryMethod());
    dto.setDeliveryAddress(order.getDeliveryAddress());
    dto.setContactInfo(order.getContactInfo());
    dto.setUserId(order.getUser().getId());
    dto.setOrderItems(order.getOrderItems().stream()
            .map(OrderItemDto::new)
            .collect(Collectors.toList()));
    dto.setStatus(order.getStatus());
    dto.setCreatedAt(order.getCreatedAt());
    dto.setUpdatedAt(order.getUpdatedAt());
    return dto;
}
}
