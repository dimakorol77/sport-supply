package org.example.services.impl;

import org.example.models.Order;
import org.example.repositories.OrderRepository;
import org.example.services.interfaces.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    // Используем конструкторную инъекцию
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Получение заказов по ID пользователя
    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Получение всех заказов
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
