package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDateTime;

@Data
public class PromotionDto {
    private Long id;

    @NotBlank(message = "The promotion name cannot be empty")
    @Size(max = 200, message = "The promotion name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description should not exceed 1000 characters")
    private String description;

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
