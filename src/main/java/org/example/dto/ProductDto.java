package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    @Schema(description = "ID продукта. Передавать только при обновлении", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;


    @NotBlank(message = "Product name cannot be empty")
    @Size(max = 200, message = "The product name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description should not exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Brand ID is required")
    private Long brandId;

    @Size(max = 50, message = "Protein type must not exceed 50 characters")
    private String proteinType;

    @Size(max = 100, message = "A group of vitamins should not exceed 100 characters")
    private String vitaminGroup;

    @Size(max = 50, message = "The form must not exceed 50 characters")
    private String form;

    public interface OnCreate {}
    public interface OnUpdate {}
}
