package org.example.services.impl;

import org.example.repositories.OrderRepository;
import org.example.services.interfaces.OrderService;
import org.springframework.stereotype.Service;
import org.example.models.Order;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImp(OrderRepository orderRepository) {
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
