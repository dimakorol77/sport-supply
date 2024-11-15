package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.annotation.cartItemController.AddItemToCart;
import org.example.annotation.cartItemController.RemoveCartItem;
import org.example.annotation.cartItemController.UpdateCartItemQuantity;
import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.models.CartItem;
import org.example.services.interfaces.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
@Validated
public class CartItemController {
    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @AddItemToCart
    public ResponseEntity<CartItemResponseDto> addItemToCart(@PathVariable Long cartId,
                                                             @RequestBody CartItemDto cartItemDto) {
        CartItemResponseDto responseDto = cartItemService.addItemToCart(cartId, cartItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @UpdateCartItemQuantity
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                                      @RequestParam Integer quantity) {
        CartItemResponseDto updatedCartItem = cartItemService.updateCartItemQuantity(cartItemId, quantity);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

    @RemoveCartItem
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
