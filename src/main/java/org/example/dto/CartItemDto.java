package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import org.example.exception.errorMessage.ErrorMessage;

@Data
public class CartItemDto {

    //данные при добавлении и обновлении товара в корзине
    @NotNull(message = ErrorMessage.CART_ITEM_PRODUCT_REQUIRED)
    private Long productId;

    @NotNull(message = ErrorMessage.CART_ITEM_QUANTITY_REQUIRED)
    @Positive(message = ErrorMessage.CART_ITEM_QUANTITY_POSITIVE)
    private Integer quantity;


    private BigDecimal discountPrice;
}
