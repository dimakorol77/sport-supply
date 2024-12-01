package org.example.controllers;

import org.example.annotations.CartItemAnnotations.AddItemToCart;
import org.example.annotations.CartItemAnnotations.RemoveCartItem;
import org.example.annotations.CartItemAnnotations.UpdateCartItemQuantity;
import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.CartItemService;
import org.example.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
@Validated
@PreAuthorize("isAuthenticated()")
public class CartItemController {
    private final CartItemService cartItemService;
    private final SecurityUtils securityUtils;

    @Autowired
    public CartItemController(CartItemService cartItemService, SecurityUtils securityUtils) {
        this.cartItemService = cartItemService;
        this.securityUtils = securityUtils;
    }
    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    @AddItemToCart
    public ResponseEntity<CartItemResponseDto> addItemToCart(@PathVariable Long cartId,
                                                             @RequestBody CartItemDto cartItemDto) {
        User user = getCurrentUser();

        CartItemResponseDto responseDto = cartItemService.addItemToCart(cartId, user.getId(), cartItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @UpdateCartItemQuantity
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                                      @RequestParam Integer quantity) {
        User user = getCurrentUser();

        CartItemResponseDto updatedCartItem = cartItemService.updateCartItemQuantity(cartItemId, user.getId(), quantity);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

    @RemoveCartItem
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        User user = getCurrentUser();

        cartItemService.removeCartItem(cartItemId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
