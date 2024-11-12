package org.example.services.impl;

import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.enums.OrderStatus;
import org.example.exception.CartAlreadyExistsException;
import org.example.exception.CartNotFoundException;
import org.example.exception.ProductNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.CartMapper;
import org.example.models.*;
import org.example.repositories.*;
import org.example.services.interfaces.CartService;
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
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository,
                           CartItemRepository cartItemRepository, OrderRepository orderRepository,
                           ProductRepository productRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
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
                .filter(cartItem -> !cartItem.isDeleted())
                .map(cartItem -> cartItem.getPrice() != null
                        ? cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    // Очистка корзины (удаление всех товаров)
    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        // Устанавливаем товары как удалённые
        cart.getCartItems().forEach(cartItem -> {
            if (!cartItem.isDeleted()) {
                cartItem.setDeleted(true);
                cartItemRepository.save(cartItem); // Сохраняем изменения по каждому товару
            }
        });

        // Используем метод calculateTotalPrice для пересчёта общей стоимости
        BigDecimal newTotalPrice = calculateTotalPrice(cartId);

        cart.setTotalPrice(newTotalPrice); // Устанавливаем пересчитанную стоимость
        cartRepository.save(cart); // Сохраняем корзину с обновленной стоимостью
    }


//    public Order convertCartToOrder(Long cartId, OrderCreateDto orderCreateDto) {
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));
//
//        // Преобразуем корзину в заказ
//        Order order = new Order();
//        order.setUser(cart.getUser());
//        order.setTotalAmount(cart.getTotalAmount());
//        order.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
//        order.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
//        order.setContactInfo(orderCreateDto.getContactInfo());
//
//        // Преобразуем товары из корзины в заказ
//        List<OrderItem> orderItems = cart.getCartItems().stream()
//                .map(cartItem -> new OrderItem(cartItem.getProduct(), cartItem.getQuantity(), cartItem.getPrice()))
//                .collect(Collectors.toList());
//
//        order.setOrderItems(orderItems);
//        order.setStatus(OrderStatus.CREATED); // Устанавливаем статус как 'Создан'
//        order.setCreatedAt(LocalDateTime.now());
//        order.setUpdatedAt(LocalDateTime.now());
//
//        // Сохраняем заказ
//        order = orderRepository.save(order);
//
//        // Очистить корзину после конвертации
//        cart.getCartItems().clear();
//        cartRepository.save(cart);
//
//        return order;
//    }
}

