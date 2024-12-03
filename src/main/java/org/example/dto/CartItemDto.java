package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import org.example.exceptions.errorMessage.ErrorMessage;

@Data
public class CartItemDto {


    @NotNull(message = ErrorMessage.CART_ITEM_PRODUCT_REQUIRED)
    private Long productId;

    @NotNull(message = ErrorMessage.CART_ITEM_QUANTITY_REQUIRED)
    @Positive(message = ErrorMessage.CART_ITEM_QUANTITY_POSITIVE)
    private Integer quantity;

}
