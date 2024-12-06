package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;
import org.example.enums.Role;
import org.example.exceptions.OrderItemNotFoundException;
import org.example.exceptions.OrderNotFoundException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.OrderItemMapper;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.OrderItemRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.OrderItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;
    private final SecurityUtils securityUtils;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderRepository orderRepository,
                                ProductRepository productRepository,
                                OrderItemMapper orderItemMapper,
                                SecurityUtils securityUtils) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemMapper = orderItemMapper;
        this.securityUtils = securityUtils;
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }
    @Transactional
    @Override
    public OrderItemDto createOrderItem(OrderItemCreateDto orderItemCreateDto, Long orderId) {
        User currentUser = getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));
        checkOrderOwnership(order, currentUser);
        Product product = getProductById(orderItemCreateDto.getProductId());
        OrderItem orderItem = orderItemMapper.toEntity(orderItemCreateDto, product);
        orderItem.setOrder(order);
        orderItem = orderItemRepository.save(orderItem);
        updateOrderTotalAmount(order);
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        User currentUser = getCurrentUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));
        checkOrderOwnership(order, currentUser);
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    @Override
    public OrderItemDto updateOrderItem(Long orderItemId, OrderItemCreateDto orderItemCreateDto) {
        User currentUser = getCurrentUser();
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException(ErrorMessage.ORDER_ITEM_NOT_FOUND));
        Order order = orderItem.getOrder();
        checkOrderOwnership(order, currentUser);
        Product product = getProductById(orderItemCreateDto.getProductId());
        orderItemMapper.updateEntityFromCreateDto(orderItemCreateDto, orderItem, product);
        orderItem = orderItemRepository.save(orderItem);
        updateOrderTotalAmount(orderItem.getOrder());

        return orderItemMapper.toDto(orderItem);
    }
    private void updateOrderTotalAmount(Order order) {
        BigDecimal totalAmount = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void deleteOrderItem(Long orderItemId) {
        User currentUser = getCurrentUser();
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException(ErrorMessage.ORDER_ITEM_NOT_FOUND));

        Order order = orderItem.getOrder();
        checkOrderOwnership(order, currentUser);
        order.getOrderItems().remove(orderItem);
        updateOrderTotalAmount(order);
        orderItemRepository.delete(orderItem);
    }



    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));
    }
    private void checkOrderOwnership(Order order, User user) {
        if (!order.getUser().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }
    }
}
