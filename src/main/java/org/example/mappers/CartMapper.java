package org.example.mappers;

import org.example.dto.CartDto;
import org.example.models.Cart;
import org.example.models.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartMapper {
    // Преобразуем Cart в CartDto
    public CartDto toDto(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartDto dto = new CartDto();
        dto.setId(cart.getId());  // ID будет присвоен после сохранения корзины
        dto.setUserId(cart.getUser().getId());  // Получаем ID пользователя
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setTotalPrice(cart.getTotalPrice());  // Устанавливаем totalPrice
        return dto;
    }

    // Преобразуем CartDto в Cart
    public Cart toEntity(CartDto dto, User user) {
        if (dto == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setUser(user);  // Связываем корзину с пользователем
        cart.setCreatedAt(dto.getCreatedAt());
        cart.setTotalPrice(dto.getTotalPrice() != null ? dto.getTotalPrice() : BigDecimal.ZERO);
        return cart;
    }

    // Обновление сущности Cart на основе CartDto
    public void updateEntityFromDto(CartDto dto, Cart cart) {
        if (dto == null || cart == null) {
            return;
        }
        cart.setTotalPrice(dto.getTotalPrice());
        cart.setCreatedAt(dto.getCreatedAt());
    }
}
