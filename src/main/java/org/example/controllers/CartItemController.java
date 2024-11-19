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
public class CartItemController {
    private final CartItemService cartItemService;
    private final UserService userService;

    @Autowired
    public CartItemController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @AddItemToCart
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartItemResponseDto> addItemToCart(@PathVariable Long cartId,
                                                             @RequestBody CartItemDto cartItemDto) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        CartItemResponseDto responseDto = cartItemService.addItemToCart(cartId, user.getId(), cartItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @UpdateCartItemQuantity
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(@PathVariable Long cartItemId,
                                                                      @RequestParam Integer quantity) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        CartItemResponseDto updatedCartItem = cartItemService.updateCartItemQuantity(cartItemId, user.getId(), quantity);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

    @RemoveCartItem
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        cartItemService.removeCartItem(cartItemId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
