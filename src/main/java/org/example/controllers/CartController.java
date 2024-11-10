package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.dto.CartDto;
import org.example.dto.UserCreateDto;
import org.example.models.Cart;
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

    @GetMapping("/{cartId}/total")
    @Operation(summary = "Подсчет общей стоимости", description = "Возвращает общую стоимость товаров в корзине", tags = "Корзина",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Общая стоимость вычислена"),
                    @ApiResponse(responseCode = "404", description = "Корзина не найдена")
            })
    public BigDecimal calculateTotalPrice(@PathVariable Long cartId) {
        return cartService.calculateTotalPrice(cartId);
    }
    @DeleteMapping("/{cartId}/clear")
    @Operation(summary = "Очистка корзины", description = "Удаляет все товары из корзины", tags = "Корзина",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Корзина успешно очищена"),
                    @ApiResponse(responseCode = "404", description = "Корзина не найдена")
            })
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
