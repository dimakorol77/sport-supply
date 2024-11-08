package org.example.services.interfaces;

import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;

import java.util.List;

public interface OrderItemService {
    OrderItem createOrderItem(OrderItemDto orderItemDto, Long orderId);
    List<OrderItem> getOrderItemsByOrderId(Long orderId); //Возвращает список элементов заказа для конкретного заказа.
    OrderItem updateOrderItem(Long orderItemId, OrderItemDto orderItemDto);
    void deleteOrderItem(Long orderItemId);
}
