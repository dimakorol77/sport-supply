package org.example.services.interfaces;

import org.apache.catalina.User;
import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.dto.UserCreateDto;
import org.example.models.Cart;
import org.example.models.Order;

import java.math.BigDecimal;

public interface CartService {
    CartDto createCart(Long userId);
    BigDecimal calculateTotalPrice(Long cartId);
    void clearCart(Long cartId);
    OrderDto convertCartToOrder(Long cartId, OrderCreateDto orderCreateDto);
    Cart getCartByUserId(Long userId);
}
