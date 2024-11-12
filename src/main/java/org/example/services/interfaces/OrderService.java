package org.example.services.interfaces;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.enums.OrderStatus;
import org.example.models.Order;

import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(Long userId);
    List<OrderDto> getAllOrders();
    OrderDto createOrder(OrderCreateDto orderCreateDto);
    OrderDto updateOrderStatus(Long orderId, OrderStatus status);
}
