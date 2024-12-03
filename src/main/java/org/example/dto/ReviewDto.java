package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

@Data
public class ReviewDto {
    @Schema(description = "ID отзыва. Передается только в URL", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private Long userId;

    @NotNull(message = "Product ID required")
    private Long productId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "The rating must be at least 1")
    @Max(value = 5, message = "The rating should not exceed 5")
    private Integer rating;

    @Size(max = 1000, message = "The comment must not exceed 1000 characters")
    private String userComment;
    public interface OnCreate {}
    public interface OnUpdate {}
}
