package org.example.services.interfaces;

import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;

import java.math.BigDecimal;

public interface CartService {
    CartDto createCart(Long userId);
    BigDecimal calculateTotalPrice(Long cartId);
    void clearCart(Long cartId, boolean skipAccessCheck);
    OrderDto convertCartToOrder(Long cartId, OrderCreateDto orderCreateDto);

}
