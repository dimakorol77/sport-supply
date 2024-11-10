package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.example.exception.errorMessage.ErrorMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartDto {
    @NotNull(message = ErrorMessage.CART_ITEM_PRODUCT_REQUIRED) // для id корзины
    private Long id;

    @NotNull(message = ErrorMessage.CART_ITEM_USER_REQUIRED) // для userId
    private Long userId;

    @NotNull(message = ErrorMessage.CART_ITEM_CREATED_AT_REQUIRED) // для createdAt
    private LocalDateTime createdAt;


}
