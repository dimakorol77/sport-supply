package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderItemDto;
import org.example.dto.OrderResponseDto;
import org.example.dto.OrderSummaryDto;
import org.example.enums.OrderStatus;
import org.example.exception.OrderNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.models.Order;
import org.example.services.interfaces.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    // Используем конструкторную инъекцию
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Получение всех заказов
    @GetMapping
    @Operation(summary = "Получение всех заказов", description = "Возвращает список всех заказов", tags = "Заказы",
            responses = {@ApiResponse(responseCode = "200", description = "Заказы найдены")}
    )
    public ResponseEntity<List<OrderSummaryDto>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderSummaryDto> orderDtos = orders.stream()
                .map(order -> {
                    List<OrderItemDto> itemsDto = order.getOrderItems().stream()
                            .map(item -> new OrderItemDto(item.getProduct().getId(), item.getProduct().getName(), item.getPrice(), item.getQuantity()))
                            .collect(Collectors.toList());
                    return new OrderSummaryDto(order.getId(), order.getTotalAmount(), order.getStatus(), order.getCreatedAt(), itemsDto);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    // Получение заказов по ID пользователя
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получение заказов по ID пользователя", description = "Возвращает список заказов для указанного пользователя", tags = "Заказы",
            responses = {@ApiResponse(responseCode = "200", description = "Заказы найдены"), @ApiResponse(responseCode = "404", description = "Пользователь не найден")}
    )
    public ResponseEntity<List<OrderSummaryDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<OrderSummaryDto> orderDtos = orders.stream()
                .map(order -> {
                    List<OrderItemDto> itemsDto = order.getOrderItems().stream()
                            .map(item -> new OrderItemDto(item.getProduct().getId(), item.getProduct().getName(), item.getPrice(), item.getQuantity()))
                            .collect(Collectors.toList());
                    return new OrderSummaryDto(order.getId(), order.getTotalAmount(), order.getStatus(), order.getCreatedAt(), itemsDto);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }
    @PostMapping
    @Operation(summary = "Создание заказа", description = "Создает новый заказ", tags = "Заказы",
            responses = {@ApiResponse(responseCode = "201", description = "Заказ успешно создан"), @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса")}
    )
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        Order order = orderService.createOrder(orderCreateDto);
        OrderResponseDto responseDto = new OrderResponseDto(order.getId(), order.getTotalAmount(), order.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{orderId}/status")
    @Operation(
            summary = "Обновление статуса заказа",
            description = "Обновляет статус указанного заказа",
            tags = "Заказы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус заказа успешно обновлен"),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден")
            }
    )
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        OrderResponseDto responseDto = new OrderResponseDto(updatedOrder.getId(), updatedOrder.getTotalAmount(), updatedOrder.getStatus());
        return ResponseEntity.ok(responseDto);
    }
}

