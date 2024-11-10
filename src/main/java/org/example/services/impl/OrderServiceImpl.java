package org.example.services.impl;

import org.example.enums.OrderStatus;
import org.example.exception.OrderNotFoundException;
import org.example.exception.ProductNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
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

    // Используем конструкторную инъекцию
public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
}

    // Получение заказов по ID пользователя
    @Override

    public List<Order> getOrdersByUserId(Long userId) {
        // Получаем все заказы пользователя по ID
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.isEmpty() ? Collections.emptyList() : orders;
    } //добавила проверку, чтобы возвращать пустой список, если заказов нет

    // Получение всех заказов
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order createOrder(OrderCreateDto orderCreateDto) {
        // Создаем объект Order
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

        // Обработка orderItems
        List<OrderItem> orderItems = orderCreateDto.getOrderItems().stream()
                .map(itemDto -> {
                    // Находим продукт
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

                    // Создаем объект OrderItem
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setPrice(itemDto.getPrice());
                    orderItem.setQuantity(itemDto.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }
//для обновления статуса заказа
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));
        order.setStatus(status);// Обновляем статус заказа
        order.setUpdatedAt(LocalDateTime.now());// Обновляем время
        return orderRepository.save(order);// Сохраняем обновленный заказ
    }
}
