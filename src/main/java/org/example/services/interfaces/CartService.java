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
    BigDecimal calculateTotalPrice(Long cartId, Long userId);
    void clearCart(Long cartId, Long userId);
    OrderDto convertCartToOrder(Long cartId, Long userId, OrderCreateDto orderCreateDto);
   // Cart getCartByUserId(Long userId);
}
