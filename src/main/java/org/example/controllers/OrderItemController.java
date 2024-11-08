package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;
import org.example.services.interfaces.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/{orderId}")
    @Operation(
            summary = "Создание элемента заказа",
            description = "Создает новый элемент для указанного заказа",
            tags = "Элементы заказа",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Элемент заказа успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден")
            }
    )
    public ResponseEntity<OrderItemDto> createOrderItem(@PathVariable Long orderId, @Valid @RequestBody OrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemService.createOrderItem(orderItemDto, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderItemDto(orderItem));
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Получение элементов заказа",
            description = "Возвращает все элементы для указанного заказа",
            tags = "Элементы заказа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список элементов заказа успешно возвращен"),
                    @ApiResponse(responseCode = "404", description = "Заказ не найден")
            }
    )
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        // Получаем элементы заказа по ID
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        // Преобразуем их в DTO и возвращаем
        List<OrderItemDto> orderItemDtos = orderItems.stream().map(OrderItemDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(orderItemDtos);
    }

    @PutMapping("/{orderItemId}")
    @Operation(
            summary = "Обновление элемента заказа",
            description = "Обновляет данные указанного элемента заказа",
            tags = "Элементы заказа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Элемент заказа успешно обновлен"),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                    @ApiResponse(responseCode = "404", description = "Элемент заказа не найден")
            }
    )
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable Long orderItemId, @Valid @RequestBody OrderItemDto orderItemDto) {
        // Обновляем элемент заказа
        OrderItem updatedOrderItem = orderItemService.updateOrderItem(orderItemId, orderItemDto);
        return ResponseEntity.ok(new OrderItemDto(updatedOrderItem));
    }

    @DeleteMapping("/{orderItemId}")
    @Operation(
            summary = "Удаление элемента заказа",
            description = "Удаляет указанный элемент заказа",
            tags = "Элементы заказа",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Элемент заказа успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Элемент заказа не найден")
            }
    )
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }
}
