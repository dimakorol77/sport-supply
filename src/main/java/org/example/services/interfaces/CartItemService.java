package org.example.services.interfaces;

import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.models.CartItem;

public interface CartItemService {
    CartItemResponseDto addItemToCart(Long cartId, Long userId, CartItemDto cartItemDto);
    CartItemResponseDto updateCartItemQuantity(Long cartItemId, Long userId, Integer quantity);
    void removeCartItem(Long cartItemId, Long userId);
}
