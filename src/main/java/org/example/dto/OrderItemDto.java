package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.models.OrderItem;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class OrderItemDto {
    //информация о каждом заказаном товаре
    @NotNull(message = ErrorMessage.INVALID_ORDER_DATA)
    private Long productId; // ID продукта

    private String productName; // Название продукта
    @NotNull(message = ErrorMessage.INVALID_ORDER_DATA)
    private BigDecimal price; // Цена продукта
    @Min(value = 1, message = ErrorMessage.INVALID_ORDER_DATA)
    private int quantity; // Количество


    // Конструктор, принимающий OrderItem для преобразования в DTO
    public OrderItemDto(OrderItem orderItem) {
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.price = orderItem.getPrice();
        this.quantity = orderItem.getQuantity();
    }
}
