package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Название категории не может быть пустым")
    @Size(max = 100, message = "Название категории не должно превышать 100 символов")
    private String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @PositiveOrZero(message = "ID родительской категории должен быть неотрицательным числом")
    private Long parentCategoryId;
}
