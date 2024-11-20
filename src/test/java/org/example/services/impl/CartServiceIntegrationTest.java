package org.example.services.impl;

import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.enums.Role;
import org.example.models.*;
import org.example.repositories.CartRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays; // Добавлен импорт

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceIntegrationTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testConvertCartToOrder_Success() {
        // Создаем пользователя
        User user = new User();
        user.setEmail("integration@example.com");
        user.setName("Integration User");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Создаем корзину
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setTotalPrice(new BigDecimal("100.00"));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(new Product()); // Здесь нужно установить продукт
        cartItem.setPrice(new BigDecimal("50.00"));
        cartItem.setQuantity(2);
        cartItem.setDeleted(false);

        cart.setCartItems(Arrays.asList(cartItem));
        cartRepository.save(cart);

        // Создаем DTO для заказа
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setDeliveryMethod(DeliveryMethod.COURIER);
        orderCreateDto.setDeliveryAddress("123 Integration St");
        orderCreateDto.setContactInfo("integration@example.com");

        // Создаем заказ из корзины через CartService
        OrderDto orderDto = cartService.convertCartToOrder(cart.getId(), user.getId(), orderCreateDto);

        assertNotNull(orderDto);
        assertEquals(OrderStatus.CREATED.name(), orderDto.getStatus());

        // Проверяем, что заказ сохранен в базе данных
        Order savedOrder = orderRepository.findById(orderDto.getId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals(user.getId(), savedOrder.getUser().getId());
        assertEquals(new BigDecimal("100.00"), savedOrder.getTotalAmount());

        // Проверяем, что корзина очищена
        Cart updatedCart = cartRepository.findById(cart.getId()).orElse(null);
        assertNotNull(updatedCart);
        assertEquals(BigDecimal.ZERO, updatedCart.getTotalPrice());
        assertTrue(updatedCart.getCartItems().stream().allMatch(CartItem::isDeleted));
    }
}

