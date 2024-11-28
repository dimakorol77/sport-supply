package org.example.controllers;



import org.example.annotations.OrderAnnotations.GetAllOrders;
import org.example.annotations.OrderAnnotations.GetOrdersByUserId;
import org.example.annotations.OrderAnnotations.UpdateOrderStatus;
import org.example.annotations.OrderAnnotations.*;

import org.example.dto.*;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.mappers.OrderMapper;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final SecurityUtils securityUtils;

    public OrderController(OrderService orderService, SecurityUtils securityUtils) {
        this.orderService = orderService;
        this.securityUtils = securityUtils;
    }
    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    @GetAllOrders
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos = orderService.getAllOrders();
        return ResponseEntity.ok(orderDtos);
    }

    @GetOrdersByUserId
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@RequestParam(required = false) Long userId) {
        List<OrderDto> orderDtos = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orderDtos);
    }

    @UpdateOrderStatus
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        OrderDto updatedOrderDto = orderService.updateOrderStatus(orderId, status);
        OrderResponseDto responseDto = new OrderResponseDto(updatedOrderDto.getId(), updatedOrderDto.getTotalAmount(), updatedOrderDto.getStatus());
        return ResponseEntity.ok(responseDto);
    }

    @GetOrderById
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        User currentUser = getCurrentUser();
        OrderDto orderDto = orderService.getOrderByIdAndCheckOwnership(orderId, currentUser.getId());
        return ResponseEntity.ok(orderDto);
    }

    @CancelOrder
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        User currentUser = getCurrentUser();
        orderService.cancelOrderAndCheckOwnership(orderId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @GetOrdersByStatus
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@RequestParam OrderStatus status) {
        List<OrderDto> orderDtos = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orderDtos);
    }

    @GetOrdersCreatedAfter
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersCreatedAfter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<OrderDto> orderDtos = orderService.getOrdersCreatedAfter(date);
        return ResponseEntity.ok(orderDtos);
    }

    @GetOrdersByDeliveryMethod
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersByDeliveryMethod(@RequestParam DeliveryMethod deliveryMethod) {
        List<OrderDto> orderDtos = orderService.getOrdersByDeliveryMethod(deliveryMethod);
        return ResponseEntity.ok(orderDtos);
    }
}

