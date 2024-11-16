package org.example.controllers;



import org.example.annotations.OrderAnnotations.GetAllOrders;
import org.example.annotations.OrderAnnotations.GetOrdersByUserId;
import org.example.annotations.OrderAnnotations.UpdateOrderStatus;
import org.example.annotations.OrderAnnotations.*;

import org.example.dto.*;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.mappers.OrderMapper;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    // Используем конструкторную инъекцию
    public OrderController(OrderService orderService, CartService cartService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.orderMapper = orderMapper;  // Инициализируем маппер
    }

    // Получение всех заказов
    @GetAllOrders
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos = orderService.getAllOrders();
        return ResponseEntity.ok(orderDtos);
    }

    // Получение заказов по ID пользователя
    @GetOrdersByUserId
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDto> orderDtos = orderService.getOrdersByUserId(userId);
        if (orderDtos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(orderDtos);
    }

    @UpdateOrderStatus
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        OrderDto updatedOrderDto = orderService.updateOrderStatus(orderId, status);
        OrderResponseDto responseDto = new OrderResponseDto(updatedOrderDto.getId(), updatedOrderDto.getTotalAmount(), updatedOrderDto.getStatus());
        return ResponseEntity.ok(responseDto);
    }

    @GetOrderById
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    // Отмена заказа
    @CancelOrder
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

    // Получение заказов по статусу
    @GetOrdersByStatus
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@RequestParam OrderStatus status) {
        List<OrderDto> orderDtos = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orderDtos);
    }

    // Получение заказов, созданных после определенной даты
    @GetOrdersCreatedAfter
    public ResponseEntity<List<OrderDto>> getOrdersCreatedAfter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<OrderDto> orderDtos = orderService.getOrdersCreatedAfter(date);
        return ResponseEntity.ok(orderDtos);
    }

    // Получение заказов по методу доставки
    @GetOrdersByDeliveryMethod
    public ResponseEntity<List<OrderDto>> getOrdersByDeliveryMethod(@RequestParam DeliveryMethod deliveryMethod) {
        List<OrderDto> orderDtos = orderService.getOrdersByDeliveryMethod(deliveryMethod);
        return ResponseEntity.ok(orderDtos);
    }
}

