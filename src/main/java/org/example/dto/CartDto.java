package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.exceptions.errorMessage.ErrorMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartDto {

    private Long id;

    @NotNull(message = ErrorMessage.CART_ITEM_USER_REQUIRED) // для userId
    private Long userId;


    private LocalDateTime createdAt;


    private BigDecimal totalPrice;


}
