package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.OrderStatus;
import org.example.exception.UserNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.models.Order;
import org.example.repositories.OrderRepository;
import org.example.services.interfaces.OrderService;
import org.springframework.stereotype.Service;
import org.example.dto.OrderCreateDto;
import org.example.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // Используем конструкторную инъекцию
//    public OrderServiceImpl(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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

    @Override
    public Order createOrder(OrderCreateDto orderCreateDto) {
        Order order = new Order();
        order.setTotalAmount(orderCreateDto.getTotalAmount());
        order.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
        order.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
        order.setContactInfo(orderCreateDto.getContactInfo());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED); // Статус устанавливается автоматически
        // Загрузка пользователя из репозитория
        User user = userRepository.findById(orderCreateDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));
        order.setUser(user);

        return orderRepository.save(order);
    }
}
