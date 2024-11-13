package org.example.controllers;

import org.example.annotations.CalculateTotalPrice;
import org.example.annotations.ClearCart;
import org.example.annotations.CreateCart;
import org.example.dto.CartDto;
import org.example.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
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
}
