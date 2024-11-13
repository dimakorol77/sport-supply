package org.example.controllers;

import jakarta.validation.Valid;
import org.example.annotations.CreateOrder;
import org.example.annotations.GetAllOrders;
import org.example.annotations.GetOrdersByUserId;
import org.example.annotations.UpdateOrderStatus;
import org.example.dto.*;
import org.example.enums.OrderStatus;
import org.example.mappers.OrderMapper;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
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
    @CreateOrder
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        OrderDto orderDto = orderService.createOrder(orderCreateDto);
        OrderResponseDto responseDto = new OrderResponseDto(orderDto.getId(), orderDto.getTotalAmount(), orderDto.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @UpdateOrderStatus
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        OrderDto updatedOrderDto = orderService.updateOrderStatus(orderId, status);
        OrderResponseDto responseDto = new OrderResponseDto(updatedOrderDto.getId(), updatedOrderDto.getTotalAmount(), updatedOrderDto.getStatus());
        return ResponseEntity.ok(responseDto);
    }


//    @PostMapping("/convert")
//@Operation(
//        summary = "Конвертация корзины в заказ",
//        description = "Преобразует корзину в заказ и очищает корзину",
//        tags = "Заказы",
//        responses = {
//                @ApiResponse(responseCode = "201", description = "Заказ успешно создан"),
//                @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса")
//        }
//)
//public ResponseEntity<OrderResponseDto> convertCartToOrder(@RequestParam Long cartId, @Valid @RequestBody OrderCreateDto orderCreateDto) {
//    OrderDto orderDto = cartService.convertCartToOrder(cartId, orderCreateDto);
//    OrderResponseDto responseDto = new OrderResponseDto(orderDto.getId(), orderDto.getTotalAmount(), orderDto.getStatus());
//    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//}
}

