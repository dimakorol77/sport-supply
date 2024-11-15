package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.OrderItemDto;
import org.example.exception.OrderItemNotFoundException;
import org.example.exception.OrderNotFoundException;
import org.example.exception.ProductNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.OrderItemMapper;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.example.repositories.OrderItemRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.example.services.interfaces.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderRepository orderRepository,
                                ProductRepository productRepository,
                                OrderItemMapper orderItemMapper) {  // Внедрение маппера
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemMapper = orderItemMapper;  // Инициализация
    }

    @Transactional
    @Override
    public OrderItem createOrderItem(OrderItemDto orderItemDto, Long orderId) {
        // Получаем заказ по ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        // Получаем продукт по ID
        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        // Создаем новый OrderItem из DTO и Product
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto, product);
        orderItem.setOrder(order);

        // Сохраняем новый OrderItem
        orderItem = orderItemRepository.save(orderItem);

        // Обновляем общую сумму заказа
        updateOrderTotalAmount(order);

        return orderItem;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
    @Transactional
    @Override
    public OrderItem updateOrderItem(Long orderItemId, OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException(ErrorMessage.ORDER_ITEM_NOT_FOUND));

        // Получаем продукт по ID
        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        // Обновляем данные OrderItem
        orderItemMapper.updateEntityFromDto(orderItemDto, orderItem, product);

        // Сохраняем обновленный OrderItem
        orderItem = orderItemRepository.save(orderItem);

        // Обновляем общую сумму заказа
        updateOrderTotalAmount(orderItem.getOrder());

        return orderItem;
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
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException(ErrorMessage.ORDER_ITEM_NOT_FOUND));

        Order order = orderItem.getOrder();
        orderItemRepository.delete(orderItem);

        // Обновляем общую сумму заказа
        updateOrderTotalAmount(order);
    }
}
