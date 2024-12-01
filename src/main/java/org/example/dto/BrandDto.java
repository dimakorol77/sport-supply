package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class BrandDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Automatically generated ID, not required in requests")
    private Long id;

    @NotBlank(message = "Brand name cannot be empty")
    @Size(max = 100, message = "The brand name should not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    public interface OnCreate {}
    public interface OnUpdate {}

}
