package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
public class OrderSummaryDto {
    //возвращаемая информация о полном заказе
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItems; // Список товаров в заказе
}
