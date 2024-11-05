package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class OrderItemDto {
    //информация о каждом заказаном товаре
    private Long productId; // ID продукта
    private String productName; // Название продукта
    private BigDecimal price; // Цена продукта
    private int quantity; // Количество
}
