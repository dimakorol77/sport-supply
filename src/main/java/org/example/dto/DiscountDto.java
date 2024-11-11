package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiscountDto {
    private Long id;

    @NotNull(message = "ID продукта обязателен")
    private Long productId;

    @NotNull(message = "Цена со скидкой обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена со скидкой должна быть больше нуля")
    private BigDecimal discountPrice;

    @NotNull(message = "Дата начала обязательна")
    @PastOrPresent(message = "Дата начала не может быть в будущем")
    private LocalDateTime startDate;

    @NotNull(message = "Дата окончания обязательна")
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDateTime endDate;

    @AssertTrue(message = "Дата окончания должна быть после даты начала")
    private boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true; // Проверку @NotNull выполняют другие аннотации
        }
        return endDate.isAfter(startDate);
    }
}
