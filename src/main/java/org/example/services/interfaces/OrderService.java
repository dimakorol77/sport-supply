package org.example.services.interfaces;
import org.example.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByUserId(Long userId);
    List<Order> getAllOrders();
}
