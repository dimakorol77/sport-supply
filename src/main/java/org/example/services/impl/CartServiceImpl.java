package org.example.services.impl;

import org.springframework.transaction.annotation.Transactional;
import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.exceptions.*;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.CartMapper;
import org.example.models.*;
import org.example.repositories.*;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final OrderService orderService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository,
                           OrderService orderService, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;

        this.orderService = orderService;
        this.cartMapper = cartMapper;
    }

    @Override
    public CartDto createCart(Long userId) {
        if (cartRepository.existsByUser_Id(userId)) {
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


        return cartMapper.toDto(savedCart);
    }

    @Override
    public BigDecimal calculateTotalPrice(Long cartId, Long userId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));


        if (!cart.getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        return cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDeleted())
                .map(cartItem -> {
                    BigDecimal itemTotalPrice = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                    BigDecimal itemDiscount = cartItem.getDiscountPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                    return itemTotalPrice.subtract(itemDiscount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .max(BigDecimal.ZERO);
    }

    @Override
    public void clearCart(Long cartId, Long userId, boolean skipAccessCheck) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));


        if (!skipAccessCheck && !cart.getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        cart.getCartItems().forEach(cartItem -> {
            if (!cartItem.isDeleted()) {
                cartItem.setDeleted(true);
            }
        });
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        }


    @Transactional
    @Override
    public OrderDto convertCartToOrder(Long cartId, Long userId, OrderCreateDto orderCreateDto) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));


        if (!cart.getUser().getId().equals(userId)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }


        boolean hasActiveItems = cart.getCartItems().stream()
                .anyMatch(cartItem -> !cartItem.isDeleted());

        if (!hasActiveItems) {
            throw new CartEmptyException(ErrorMessage.CART_EMPTY);
        }


        OrderDto orderDto = orderService.createOrderFromCart(cart, orderCreateDto);


        clearCart(cartId, userId, true);

        return orderDto;
    }



}

