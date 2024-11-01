package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.services.impl.OrderServiceImp;
import org.example.models.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderServiceImp orderServiceImp;

    public OrderController(OrderServiceImp orderServiceImp) {
        this.orderServiceImp = orderServiceImp;
    }
    // для получения всех заказов
    @GetMapping
    @Operation(summary = "Получение всех заказов",
            description = "Возвращает список всех заказов",
            tags = "Заказы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказы найдены")
            },
            hidden = false
    )
    public List<Order> getAllOrders() {
        return orderServiceImp.getAllOrders();
    }

    // для получения заказов по ID пользователя
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получение заказов по ID пользователя",
            description = "Возвращает список заказов для указанного пользователя",
            tags = "Заказы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заказы найдены"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            },
            hidden = false
    )
    public List<Order> getOrdersByUserId(@PathVariable Long userId) {
        return orderServiceImp.getOrdersByUserId(userId);
    }
}
