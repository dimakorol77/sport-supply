package org.example.services.interfaces;

import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.models.CartItem;

public interface CartItemService {
    CartItemResponseDto addItemToCart(Long cartId, CartItemDto cartItemDto);
    CartItemResponseDto updateCartItemQuantity(Long cartItemId, Integer quantity);
    void removeCartItem(Long cartItemId);
}
