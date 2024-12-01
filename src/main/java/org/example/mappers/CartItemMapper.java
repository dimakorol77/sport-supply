package org.example.mappers;

import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapper {
    public CartItemResponseDto toResponseDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setProductId(cartItem.getProduct().getId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getPrice());
        dto.setDiscountPrice(cartItem.getDiscountPrice());
        dto.setName(cartItem.getProduct().getName());
        return dto;
    }

    public CartItem toEntity(CartItemDto dto, Cart cart, Product product) {
        if (dto == null) {
            return null;
        }
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setDeleted(false);

        return cartItem;
    }

    public void updateEntityFromDto(CartItemDto dto, CartItem cartItem) {
        if (dto == null || cartItem == null) {
            return;
        }
        cartItem.setQuantity(dto.getQuantity());

    }

}
