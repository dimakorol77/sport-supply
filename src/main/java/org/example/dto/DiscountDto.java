package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.AssertTrue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiscountDto {
    @Schema(description = "ID скидки. Передается только в URL", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(groups = OnCreate.class, message = "Product ID required")
    private Long productId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Discount price required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount price must be greater than zero")
    private BigDecimal discountPrice;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Start date required")
    @PastOrPresent(groups = OnCreate.class, message = "Start date cannot be in the future")
    private LocalDateTime startDate;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "End date required")
    @Future(groups = {OnCreate.class, OnUpdate.class}, message = "The end date must be in the future")
    private LocalDateTime endDate;

    @AssertTrue(message = "The end date must be after the start date")
    private boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }

    public interface OnCreate {}
    public interface OnUpdate {}
}

