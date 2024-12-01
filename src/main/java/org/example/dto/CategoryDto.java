package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class CategoryDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;


    @NotBlank(message = "Category name cannot be empty")
    @Size(max = 100, message = "The category name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @PositiveOrZero(message = "The parent category ID must be a non-negative number", groups = {OnCreate.class, OnUpdate.class})
    private Long parentCategoryId;


    public interface OnCreate {}
    public interface OnUpdate {}
}
