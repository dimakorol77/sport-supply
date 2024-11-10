package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {
    //для ответа на запрос добавления товара
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String name;
}
