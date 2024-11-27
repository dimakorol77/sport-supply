// src/test/java/org/example/services/impl/CartServiceImplTest.java

package org.example.services.impl;

import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.exceptions.CartAlreadyExistsException;
import org.example.exceptions.CartEmptyException;
import org.example.exceptions.CartNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.CartMapper;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.User;
import org.example.repositories.CartItemRepository;
import org.example.repositories.CartRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private CartDto cartDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setCartItems(new ArrayList<>());

        cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setUserId(1L);
        cartDto.setCreatedAt(LocalDateTime.now());
        cartDto.setTotalPrice(BigDecimal.ZERO);
    }

    @Test
    void testCreateCart_Success() {
        when(cartRepository.existsByUserId(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartMapper.toEntity(any(CartDto.class), eq(user))).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(cartDto);

        CartDto result = cartService.createCart(1L);

        assertNotNull(result);
        assertEquals(cartDto, result);
        verify(cartRepository, times(2)).save(any(Cart.class)); // Initial save and after updating total price
    }

    @Test
    void testCreateCart_AlreadyExists() {
        when(cartRepository.existsByUserId(1L)).thenReturn(true);

        CartAlreadyExistsException exception = assertThrows(CartAlreadyExistsException.class, () -> cartService.createCart(1L));
        assertEquals(ErrorMessage.CART_ALREADY_EXISTS, exception.getMessage());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testCreateCart_UserNotFound() {
        when(cartRepository.existsByUserId(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> cartService.createCart(1L));
        assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCalculateTotalPrice_Success() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        BigDecimal totalPrice = cartService.calculateTotalPrice(1L, 1L);

        assertNotNull(totalPrice);
        assertEquals(BigDecimal.ZERO, totalPrice);
    }

    @Test
    void testCalculateTotalPrice_CartNotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> cartService.calculateTotalPrice(1L, 1L));
        assertEquals(ErrorMessage.CART_NOT_FOUND, exception.getMessage());
    }

//    @Test
//    void testClearCart_Success() {
//        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
//
//        assertDoesNotThrow(() -> cartService.clearCart(1L, 1L));
//        verify(cartRepository, times(1)).save(cart);
//    }

//    @Test
//    void testClearCart_CartNotFound() {
//        when(cartRepository.findById(1L)).thenReturn(Optional.empty());
//
//        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> cartService.clearCart(1L, 1L));
//        assertEquals(ErrorMessage.CART_NOT_FOUND, exception.getMessage());
//    }

    @Test
    void testConvertCartToOrder_CartNotFound() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> cartService.convertCartToOrder(1L, 1L, new OrderCreateDto()));
        assertEquals(ErrorMessage.CART_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testConvertCartToOrder_EmptyCart() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        CartEmptyException exception = assertThrows(CartEmptyException.class, () -> cartService.convertCartToOrder(1L, 1L, new OrderCreateDto()));
        assertEquals(ErrorMessage.CART_EMPTY, exception.getMessage());
    }

    @Test
    void testConvertCartToOrder_Success() {
        cart.getCartItems().add(new CartItem()); // Добавляем элемент, чтобы корзина не была пустой
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(orderService.createOrderFromCart(eq(cart), any(OrderCreateDto.class))).thenReturn(new OrderDto());

        OrderDto result = cartService.convertCartToOrder(1L, 1L, new OrderCreateDto());

        assertNotNull(result);
        verify(orderService, times(1)).createOrderFromCart(eq(cart), any(OrderCreateDto.class));
        verify(cartRepository, times(1)).save(cart); // Проверяем, что корзина обновлена после создания заказа
    }
}
