package org.example.services.interfaces;
import org.example.dto.OrderCreateDto;
import org.example.enums.OrderStatus;
import org.example.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getAllOrders();
    Order createOrder(OrderCreateDto orderCreateDto);
    Order updateOrderStatus(Long orderId, OrderStatus status);
}
