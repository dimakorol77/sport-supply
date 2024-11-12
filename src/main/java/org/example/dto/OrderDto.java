package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.exception.errorMessage.ErrorMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    @NotNull(message = "Общая сумма заказа не может быть пустой")
    @DecimalMin(value = "0.01", message = "Сумма заказа должна быть больше нуля")
    private BigDecimal totalAmount;  // Общая сумма заказа

    @NotNull(message = "Метод доставки не может быть пустым")
    private DeliveryMethod deliveryMethod;  // Метод доставки

    @NotBlank(message = "Адрес доставки не может быть пустым")
    private String deliveryAddress;  // Адрес доставки

    @NotBlank(message = "Контактная информация не может быть пустой")
    private String contactInfo;  // Контактная информация

    private Long userId;  // ID пользователя

    private List<OrderItemDto> orderItems;  // Список товаров в заказе

    private OrderStatus status;  // Статус заказа

    private LocalDateTime createdAt;  // Дата создания заказа

    private LocalDateTime updatedAt;  // Дата обновления заказа
}

