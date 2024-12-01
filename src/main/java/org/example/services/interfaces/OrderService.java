package org.example.services.interfaces;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.models.Cart;
import org.example.models.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<OrderDto> getOrdersByUserId(Long userId);
    List<OrderDto> getAllOrders();
    OrderDto createOrderFromCart(Cart cart, OrderCreateDto orderCreateDto);
    OrderDto updateOrderStatus(Long orderId, OrderStatus status);
    List<OrderDto> getOrdersByStatus(OrderStatus status);
    List<OrderDto> getOrdersCreatedAfter(LocalDateTime date);
    List<OrderDto> getOrdersByDeliveryMethod(DeliveryMethod deliveryMethod);
    boolean isOrderOwner(Long orderId, Long userId);
    OrderDto getOrderByIdAndCheckOwnership(Long orderId, Long userId);
    void cancelOrderAndCheckOwnership(Long orderId, Long userId);
    OrderDto updateOrderStatusBySystem(Long orderId, OrderStatus status);
}
