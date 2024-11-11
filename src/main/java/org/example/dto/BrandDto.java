package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class BrandDto {
    private Long id;

    @NotBlank(message = "Название бренда не может быть пустым")
    @Size(max = 100, message = "Название бренда не должно превышать 100 символов")
    private String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;
}
