package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "Название продукта не может быть пустым")
    @Size(max = 200, message = "Название продукта не должно превышать 200 символов")
    private String name;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше нуля")
    private BigDecimal price;

    @NotNull(message = "ID категории обязателен")
    private Long categoryId;

    @NotNull(message = "ID бренда обязателен")
    private Long brandId;

    @Size(max = 50, message = "Тип белка не должен превышать 50 символов")
    private String proteinType;

    @Size(max = 100, message = "Группа витаминов не должна превышать 100 символов")
    private String vitaminGroup;

    @Size(max = 50, message = "Форма не должна превышать 50 символов")
    private String form;
}
