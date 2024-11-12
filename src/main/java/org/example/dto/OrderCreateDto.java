package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.DeliveryMethod;
import org.example.exception.errorMessage.ErrorMessage;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateDto {
    //информация, необходимая для создания нового заказа.
    // - Добавить сам товар
    @NotNull(message = ErrorMessage.INVALID_ORDER_DATA)
    @DecimalMin(value = "0.01", message = "Сумма заказа должна быть больше нуля")
    private BigDecimal totalAmount;

    @NotNull(message = ErrorMessage.INVALID_ORDER_DATA)
    private DeliveryMethod deliveryMethod;

    @NotBlank(message = ErrorMessage.INVALID_ORDER_DATA)
    private String deliveryAddress;

    @NotBlank(message = ErrorMessage.INVALID_ORDER_DATA)
    private String contactInfo;

    private Long userId;

     @NotEmpty(message = ErrorMessage.INVALID_ORDER_DATA)
    private List<OrderItemDto> orderItems; //передаем товары в заказе при его создании
}
