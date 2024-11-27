package org.example.controllers;

import jakarta.validation.Valid;

import org.example.annotations.OrderItemAnnotations.CreateOrderItem;
import org.example.annotations.OrderItemAnnotations.DeleteOrderItem;
import org.example.annotations.OrderItemAnnotations.GetOrderItemsByOrderId;
import org.example.annotations.OrderItemAnnotations.UpdateOrderItem;

import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;

import org.example.services.interfaces.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order-items")
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @CreateOrderItem
    public ResponseEntity<OrderItemDto> createOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemCreateDto orderItemCreateDto) {
        OrderItemDto createdItem = orderItemService.createOrderItem(orderItemCreateDto, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @GetOrderItemsByOrderId
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(
            @PathVariable Long orderId) {
        List<OrderItemDto> orderItemDtos = orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItemDtos);
    }

    @UpdateOrderItem
    public ResponseEntity<OrderItemDto> updateOrderItem(
            @PathVariable Long orderItemId,
            @Valid @RequestBody OrderItemCreateDto orderItemCreateDto) {
        OrderItemDto updatedItem = orderItemService.updateOrderItem(orderItemId, orderItemCreateDto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteOrderItem
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}
