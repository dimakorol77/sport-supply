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
        dto.setProductId(cartItem.getProduct().getId());  // ID товара
        dto.setQuantity(cartItem.getQuantity());  // Количество товара
        dto.setPrice(cartItem.getPrice());  // Цена товара
        dto.setDiscountPrice(cartItem.getDiscountPrice());  // Цена со скидкой
        dto.setName(cartItem.getProduct().getName());  // Название товара
        return dto;
    }

    // Преобразуем CartItemDto в CartItem
    public CartItem toEntity(CartItemDto dto, Cart cart, Product product) {
        if (dto == null) {
            return null;
        }
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);  // Связываем товар с корзиной
        cartItem.setProduct(product);  // Связываем товар с продуктом
        cartItem.setQuantity(dto.getQuantity());  // Устанавливаем количество товара
        cartItem.setDiscountPrice(dto.getDiscountPrice());  // Устанавливаем цену со скидкой (если есть)
        cartItem.setPrice(calculateFinalPrice(cartItem, dto.getDiscountPrice()));  // Вычисляем цену товара с учетом скидки
        cartItem.setDeleted(false);  // По умолчанию товар не удален
        return cartItem;
    }

    // Обновление сущности CartItem на основе CartItemDto
    public void updateEntityFromDto(CartItemDto dto, CartItem cartItem) {
        if (dto == null || cartItem == null) {
            return;
        }
        cartItem.setQuantity(dto.getQuantity());  // Обновляем количество товара
        cartItem.setDiscountPrice(dto.getDiscountPrice());  // Обновляем цену со скидкой
        cartItem.setPrice(calculateFinalPrice(cartItem, dto.getDiscountPrice()));  // Пересчитываем цену с учетом скидки
    }

    // Метод для вычисления итоговой цены товара с учетом скидки
    private BigDecimal calculateFinalPrice(CartItem cartItem, BigDecimal discountPrice) {
        BigDecimal itemPrice = cartItem.getProduct().getPrice();
        BigDecimal finalDiscountPrice = discountPrice != null ? discountPrice : BigDecimal.ZERO;
        return itemPrice.subtract(finalDiscountPrice).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }
}
