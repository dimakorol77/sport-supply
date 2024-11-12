package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.PaymentStatus;

import java.math.BigDecimal;
@Data
public class PaymentDto {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;

    private PaymentStatus status;
}
