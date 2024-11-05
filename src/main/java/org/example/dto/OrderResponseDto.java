package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.enums.OrderStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderResponseDto {
    //ответ при создании заказа
    private Long id;
    private BigDecimal totalAmount;  // Общая сумма
    private OrderStatus status;
}
