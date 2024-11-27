package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

@Data
public class ReviewDto {
    private Long id;

    @NotNull(message = "User ID required")
    private Long userId;

    @NotNull(message = "Product ID required")
    private Long productId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "The rating must be at least 1")
    @Max(value = 5, message = "The rating should not exceed 5")
    private Integer rating;

    @Size(max = 1000, message = "The comment must not exceed 1000 characters")
    private String userComment;
}
