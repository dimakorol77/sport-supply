package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.models.CartItem;
import org.example.services.interfaces.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @Operation(summary = "Добавление товара в корзину", description = "Добавляет новый товар в корзину", tags = "Корзина",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Товар успешно добавлен в корзину"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные запроса")
            })
    @PostMapping("/{cartId}")
    public ResponseEntity<CartItemResponseDto> addItemToCart(@PathVariable Long cartId,
                                                             @RequestBody CartItemDto cartItemDto) {
        CartItemResponseDto responseDto = cartItemService.addItemToCart(cartId, cartItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Обновление количества товара в корзине", description = "Обновляет количество товара в корзине", tags = "Корзина",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Количество товара обновлено"),
                    @ApiResponse(responseCode = "404", description = "Товар в корзине не найден")
            })
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                                      @RequestParam Integer quantity) {
        CartItemResponseDto updatedCartItem = cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

    @Operation(summary = "Удаление товара из корзины", description = "Удаляет товар из корзины", tags = "Корзина",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Товар удален из корзины"),
                    @ApiResponse(responseCode = "404", description = "Товар в корзине не найден")
            })
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
