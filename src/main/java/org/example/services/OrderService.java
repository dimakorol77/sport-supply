package org.example.services;

import org.example.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.example.models.Order;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // для получения заказов по ID пользователя
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // получение всех заказов
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
