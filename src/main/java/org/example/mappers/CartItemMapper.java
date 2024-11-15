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
    // Преобразуем CartItem в CartItemResponseDto
    public CartItemResponseDto toResponseDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setProductId(cartItem.getProduct().getId());     // ID товара
        dto.setQuantity(cartItem.getQuantity());             // Количество товара
        dto.setPrice(cartItem.getPrice());                   // Цена товара
        dto.setDiscountPrice(cartItem.getDiscountPrice());   // Сумма скидки
        dto.setName(cartItem.getProduct().getName());        // Название товара
        return dto;
    }

    public CartItem toEntity(CartItemDto dto, Cart cart, Product product) {
        if (dto == null) {
            return null;
        }
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);                          // Связываем товар с корзиной
        cartItem.setProduct(product);                    // Связываем товар с продуктом
        cartItem.setQuantity(dto.getQuantity());         // Устанавливаем количество товара
        cartItem.setDeleted(false);                      // По умолчанию товар не удален
        // Цена и скидка будут установлены в сервисе
        return cartItem;
    }
    // Обновление сущности CartItem на основе CartItemDto
    public void updateEntityFromDto(CartItemDto dto, CartItem cartItem) {
        if (dto == null || cartItem == null) {
            return;
        }
        cartItem.setQuantity(dto.getQuantity());  // Обновляем количество товара
        // Цена и скидка будут обновлены в сервисе
    }

}
