package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Category name cannot be empty")
    @Size(max = 100, message = "The category name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @PositiveOrZero(message = "The parent category ID must be a non-negative number")
    private Long parentCategoryId;
}
