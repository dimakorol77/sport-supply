package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCreateDto {
    @NotNull(message = "Product ID cannot be empty")
    private Long productId;

    @NotNull(message = "Price cannot be empty")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "The quantity must be at least 1")
    private int quantity;
}
