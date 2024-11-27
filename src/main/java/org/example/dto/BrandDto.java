package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class BrandDto {
    private Long id;

    @NotBlank(message = "Brand name cannot be empty")
    @Size(max = 100, message = "The brand name should not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;
}
