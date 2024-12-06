package org.example.controllers;


import jakarta.validation.Valid;

import org.example.annotations.CartAnnotations.CalculateTotalPrice;
import org.example.annotations.CartAnnotations.ClearCart;
import org.example.annotations.CartAnnotations.ConvertCartToOrder;


import org.example.dto.*;


import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@Validated
@PreAuthorize("isAuthenticated()")
public class CartController {
    private final CartService cartService;


    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;

    }


    @CalculateTotalPrice
    public BigDecimal calculateTotalPrice(@PathVariable Long cartId) {
        return cartService.calculateTotalPrice(cartId);
    }
    @ClearCart
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId, false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ConvertCartToOrder
    public ResponseEntity<OrderResponseDto> convertCartToOrder(
            @PathVariable Long cartId,
            @RequestBody @Valid OrderCreateDto orderCreateDto) {
        OrderDto orderDto = cartService.convertCartToOrder(cartId, orderCreateDto);
        OrderResponseDto responseDto = new OrderResponseDto(
                orderDto.getId(),
                orderDto.getTotalAmount(),
                orderDto.getStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
