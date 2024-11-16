package org.example.controllers;


import jakarta.validation.Valid;

import org.example.annotations.CartAnnotations.CalculateTotalPrice;
import org.example.annotations.CartAnnotations.ClearCart;
import org.example.annotations.CartAnnotations.ConvertCartToOrder;


import org.example.annotations.CartAnnotations.CreateCart;
import org.example.dto.*;


import org.example.dto.CartDto;

import org.example.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @CreateCart
    public ResponseEntity<CartDto> createCart(@PathVariable Long userId) {
        // Вызов сервиса для создания корзины и получения DTO
        CartDto cartDto = cartService.createCart(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    @CalculateTotalPrice
    public BigDecimal calculateTotalPrice(@PathVariable Long cartId) {
        return cartService.calculateTotalPrice(cartId);
    }
    @ClearCart
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ConvertCartToOrder
    public ResponseEntity<OrderResponseDto> convertCartToOrder(
            @PathVariable Long cartId,               // @PathVariable добавлено для cartId
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
