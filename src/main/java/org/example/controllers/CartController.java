package org.example.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.annotation.cartController.CalculateTotalPrice;
import org.example.annotation.cartController.ClearCart;
import org.example.annotation.cartController.ConvertCartToOrder;
import org.example.annotation.cartController.CreateCart;
import org.example.dto.*;


import org.example.dto.CartDto;

import org.example.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
