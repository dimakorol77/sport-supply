package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.models.Order;
import org.example.services.interfaces.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "Получение всех заказов",
            description = "Возвращает список всех заказов",
            tags = "Заказы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказы найдены")
            }
    )
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Получение заказов по ID пользователя
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получение заказов по ID пользователя",
            description = "Возвращает список заказов для указанного пользователя",
            tags = "Заказы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказы найдены"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    public List<Order> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }
}
