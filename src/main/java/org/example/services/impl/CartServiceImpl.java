package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.enums.OrderStatus;
import org.example.exception.*;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.CartMapper;
import org.example.models.*;
import org.example.repositories.*;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository,
                           CartItemRepository cartItemRepository, OrderRepository orderRepository,
                           OrderService orderService, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.cartMapper = cartMapper;
    }
    // Создание новой корзины для пользователя? - при регистрации пригодится, при входе
    @Override
    public CartDto createCart(Long userId) {
        if (cartRepository.existsByUserId(userId)) {
            throw new CartAlreadyExistsException(ErrorMessage.CART_ALREADY_EXISTS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));

        CartDto cartDto = new CartDto();
        cartDto.setUserId(userId);
        cartDto.setCreatedAt(LocalDateTime.now());
        cartDto.setTotalPrice(BigDecimal.ZERO);

        Cart cart = cartMapper.toEntity(cartDto, user);
        Cart savedCart = cartRepository.save(cart);

        BigDecimal totalPrice = calculateTotalPrice(savedCart.getId());
        savedCart.setTotalPrice(totalPrice);

        cartRepository.save(savedCart);

        return cartMapper.toDto(savedCart);
    }
    // Метод для подсчёта общей стоимости корзины
    @Override
    public BigDecimal calculateTotalPrice(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        return cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDeleted())  // Игнорируем удаленные элементы
                .map(cartItem -> cartItem.getPrice() != null
                        ? cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))  // Учитываем количество
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    // Очистка корзины (удаление всех товаров)
    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        cart.getCartItems().forEach(cartItem -> {
            if (!cartItem.isDeleted()) {
                cartItem.setDeleted(true);
                cartItemRepository.save(cartItem);  // Сохраняем изменения для каждого товара
            }
        });

        // Обновляем стоимость корзины после очистки
        cart.setTotalPrice(BigDecimal.ZERO);  // Обнуляем сумму
        cartRepository.save(cart);  // Сохраняем корзину
    }
    @Transactional
    @Override
    public OrderDto convertCartToOrder(Long cartId, OrderCreateDto orderCreateDto) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        // Проверяем, есть ли в корзине активные (не удаленные) товары
        boolean hasActiveItems = cart.getCartItems().stream()
                .anyMatch(cartItem -> !cartItem.isDeleted());

        if (!hasActiveItems) {
            throw new CartEmptyException(ErrorMessage.CART_EMPTY);
        }

        // Используем метод сервиса заказа для создания нового заказа из корзины и OrderCreateDto
        OrderDto orderDto = orderService.createOrderFromCart(cart, orderCreateDto);

        // Обновляем корзину после создания заказа
        updateCartAfterOrderCreation(cart);

        return orderDto;
    }

    // Метод обновления корзины после создания заказа
    private void updateCartAfterOrderCreation(Cart cart) {
        cart.getCartItems().forEach(cartItem -> {
            if (!cartItem.isDeleted()) {
                cartItem.setDeleted(true);
                cartItemRepository.save(cartItem);  // Сохраняем изменения для каждого товара
            }
        });

        // Обновляем сумму корзины
        cart.setTotalPrice(BigDecimal.ZERO);  // Обнуляем сумму
        cartRepository.save(cart);  // Сохраняем корзину
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));
    }
}

