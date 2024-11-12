package org.example.services.impl;

import org.example.dto.OrderDto;
import org.example.enums.OrderStatus;
import org.example.exception.OrderNotFoundException;
import org.example.exception.ProductNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.OrderMapper;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.example.models.User;
import org.example.models.Order;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.example.services.interfaces.OrderService;
import org.springframework.stereotype.Service;
import org.example.dto.OrderCreateDto;
import org.example.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    // Используем конструкторную инъекцию
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    // Получение заказов по ID пользователя
    @Override

    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        // Преобразуем список заказов в DTO
        return orders.isEmpty() ? Collections.emptyList() : orders.stream()
                .map(orderMapper::toDto) // Преобразуем каждый заказ в DTO
                .collect(Collectors.toList());
    }//добавила проверку, чтобы возвращать пустой список, если заказов нет

    // Получение всех заказов
    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        // Преобразуем все заказы в DTO
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto createOrder(OrderCreateDto orderCreateDto) {
        // Получаем пользователя
        User user = userRepository.findById(orderCreateDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));

        // Используем маппер для преобразования OrderCreateDto в сущность Order
        Order order = orderMapper.toEntity(orderCreateDto, user);

        // Сохраняем заказ в репозитории
        order = orderRepository.save(order);

        // Возвращаем созданный заказ в виде DTO
        return orderMapper.toDto(order);
    }
//для обновления статуса заказа
public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

    order.setStatus(status); // Обновляем статус заказа
    order.setUpdatedAt(LocalDateTime.now()); // Обновляем время

    // Сохраняем обновленный заказ
    order = orderRepository.save(order);

    // Возвращаем обновленный заказ в виде DTO
    return orderMapper.toDto(order);
}
}
