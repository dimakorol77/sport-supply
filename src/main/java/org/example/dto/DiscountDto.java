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

    @NotNull(message = "Product ID required")
    private Long productId;

    @NotNull(message = "Discount price required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount price must be greater than zero")
    private BigDecimal discountPrice;

    @NotNull(message = "Start date required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date required")
    @Future(message = "The end date must be in the future")
    private LocalDateTime endDate;

    @AssertTrue(message = "The end date must be after the start date")
    private boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }
}
