package org.example.mappers;

import org.example.dto.CartDto;
import org.example.models.Cart;
import org.example.models.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class CartMapper {

    public CartDto toDto(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }


    public Cart toEntity(CartDto dto, User user) {
        if (dto == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        cart.setTotalPrice(dto.getTotalPrice() != null ? dto.getTotalPrice() : BigDecimal.ZERO);
        return cart;
    }


    public void updateEntityFromDto(CartDto dto, Cart cart) {
        if (dto == null || cart == null) {
            return;
        }
        cart.setTotalPrice(dto.getTotalPrice());
    }
}
