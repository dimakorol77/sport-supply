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
    @NotNull(message = "ID продукта не может быть пустым")
    private Long productId;

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.0", inclusive = true, message = "Цена не может быть отрицательной")
    private BigDecimal price;

    @NotNull(message = "Количество не может быть пустым")
    @Min(value = 1, message = "Количество должно быть не менее 1")
    private int quantity;
}
