package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.OrderDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentStatus;
import org.example.enums.Role;
import org.example.exception.*;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.exception.OrderCancellationException;
import org.example.mappers.OrderMapper;
import org.example.models.*;
import org.example.repositories.*;
import org.example.services.interfaces.OrderService;
import org.example.services.interfaces.PaymentService;
import org.springframework.stereotype.Service;
import org.example.dto.OrderCreateDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final PaymentService paymentService;

    // Используем конструкторную инъекцию
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            ProductRepository productRepository, OrderMapper orderMapper,
                            OrderStatusHistoryRepository orderStatusHistoryRepository,
                            CartRepository cartRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
        this.cartRepository = cartRepository;
        this.paymentService = paymentService; // Добавляем PaymentService
    }

    // Получение заказов по ID пользователя
    @Override
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.isEmpty() ? Collections.emptyList() : orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setStatus(status);
        statusHistory.setChangedAt(LocalDateTime.now());

        if (order.getStatusHistory() == null) {
            order.setStatusHistory(new ArrayList<>());
        }
        order.getStatusHistory().add(statusHistory);

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto createOrderFromCart(Cart cart, OrderCreateDto orderCreateDto) {
        boolean hasActiveItems = cart.getCartItems().stream()
                .anyMatch(cartItem -> !cartItem.isDeleted());

        if (!hasActiveItems) {
            throw new CartEmptyException(ErrorMessage.CART_EMPTY);
        }

        Order order = createOrder(cart, orderCreateDto);
        order = orderRepository.save(order);

        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);

        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setStatus(order.getStatus());
        statusHistory.setChangedAt(LocalDateTime.now());
        orderStatusHistoryRepository.save(statusHistory);

        return orderMapper.toDto(order);
    }

    private Order createOrder(Cart cart, OrderCreateDto orderCreateDto) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalAmount(cart.getTotalPrice());
        order.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
        order.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
        order.setContactInfo(orderCreateDto.getContactInfo());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDeleted())
                .map(cartItem -> createOrderItem(cartItem, order))
                .collect(Collectors.toList());
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        Product product = cartItem.getProduct();

        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
        orderItem.setProductCategoryName(
                product.getCategory() != null ? product.getCategory().getName() : null
        );
        orderItem.setPrice(cartItem.getPrice());
        orderItem.setQuantity(cartItem.getQuantity());

        return orderItem;
    }

    @Override
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersCreatedAfter(LocalDateTime date) {
        List<Order> orders = orderRepository.findByCreatedAtAfter(date);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByDeliveryMethod(DeliveryMethod deliveryMethod) {
        List<Order> orders = orderRepository.findByDeliveryMethod(deliveryMethod);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isOrderOwner(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getUser() == null) {
            return false;
        }
        return order.getUser().getId().equals(userId);
    }

    @Override
    public OrderDto getOrderByIdAndCheckOwnership(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId) && !hasAdminRole(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);

        }

        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public void cancelOrderAndCheckOwnership(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId) && !hasAdminRole(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        if (order.getStatus() == OrderStatus.WAITING_PAYMENT || order.getStatus() == OrderStatus.CREATED) {
            order.setStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(LocalDateTime.now());

            OrderStatusHistory statusHistory = new OrderStatusHistory();
            statusHistory.setOrder(order);
            statusHistory.setStatus(OrderStatus.CANCELLED);
            statusHistory.setChangedAt(LocalDateTime.now());

            if (order.getStatusHistory() == null) {
                order.setStatusHistory(new ArrayList<>());
            }
            order.getStatusHistory().add(statusHistory);

            orderRepository.save(order);

        } else {
            throw new OrderCancellationException(ErrorMessage.ORDER_CANNOT_BE_CANCELLED);
        }
    }

    private boolean hasAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));
        return user.getRole() == Role.ADMIN;
    }
}

