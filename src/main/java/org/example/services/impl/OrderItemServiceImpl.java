package org.example.services.impl;

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

    @Override
    public OrderItem createOrderItem(OrderItemDto orderItemDto, Long orderId) {
        // Получаем заказ по ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ErrorMessage.ORDER_NOT_FOUND));

        // Получаем продукт по ID
        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        // Создаем новый OrderItem из DTO
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);  // Используем маппер
        orderItem.setOrder(order);
        orderItem.setProduct(product);

        // Сохраняем новый OrderItem в базе данных
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
    @Override
    public OrderItem updateOrderItem(Long orderItemId, OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderItemNotFoundException(ErrorMessage.ORDER_ITEM_NOT_FOUND));

        // Получаем продукт по ID
        Product product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        // Обновляем данные OrderItem
        orderItem.setProduct(product);
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());

        // Сохраняем обновленный элемент заказа
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        // Проверяем существует ли OrderItem по ID
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new OrderItemNotFoundException(ErrorMessage.ORDER_ITEM_NOT_FOUND);
        }
        // Удаляем OrderItem по ID
        orderItemRepository.deleteById(orderItemId);
    }
}
