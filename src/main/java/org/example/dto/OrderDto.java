package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    @NotNull(message = "Order total cannot be empty")
    @DecimalMin(value = "0.01", message = "The order amount must be greater than zero")
    private BigDecimal totalAmount;

    @NotNull(message = "Shipping method cannot be empty")
    private DeliveryMethod deliveryMethod;

    @NotBlank(message = "Delivery address cannot be empty")
    private String deliveryAddress;

    @NotBlank(message = "Contact information cannot be empty")
    private String contactInfo;
    private Long userId;

    private List<OrderItemDto> orderItems;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

