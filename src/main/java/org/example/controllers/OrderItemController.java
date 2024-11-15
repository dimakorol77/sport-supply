package org.example.controllers;

import jakarta.validation.Valid;

import org.example.annotation.orderItem.CreateOrderItem;
import org.example.annotation.orderItem.DeleteOrderItem;
import org.example.annotation.orderItem.GetOrderItemsByOrderId;
import org.example.annotation.orderItem.UpdateOrderItem;

import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;
import org.example.services.interfaces.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-items")
@Validated
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @CreateOrderItem
    public ResponseEntity<OrderItemDto> createOrderItem(@PathVariable Long orderId, @Valid @RequestBody OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemService.createOrderItem(orderItemDto, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderItemDto(orderItem));
    }

    @GetOrderItemsByOrderId
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(OrderItemDto::new)  // Преобразуем сущности в DTO
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderItemDtos);
    }

    @UpdateOrderItem
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable Long orderItemId, @Valid @RequestBody OrderItemDto orderItemDto) {
        OrderItem updatedOrderItem = orderItemService.updateOrderItem(orderItemId, orderItemDto);
        return ResponseEntity.ok(new OrderItemDto(updatedOrderItem));
    }

    @DeleteOrderItem
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}
