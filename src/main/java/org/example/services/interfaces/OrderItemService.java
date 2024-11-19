package org.example.services.interfaces;

import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;

import java.util.List;

public interface OrderItemService {
    OrderItemDto createOrderItem(OrderItemCreateDto orderItemCreateDto, Long orderId);
    List<OrderItemDto> getOrderItemsByOrderId(Long orderId); //Возвращает список элементов заказа для конкретного заказа.
    OrderItemDto updateOrderItem(Long orderItemId, OrderItemCreateDto orderItemCreateDto);
    void deleteOrderItem(Long orderItemId);
}
