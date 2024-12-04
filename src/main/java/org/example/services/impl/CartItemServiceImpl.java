package org.example.services.impl;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.dto.DiscountDto;
import org.example.dto.PromotionDto;
import org.example.exceptions.*;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.CartItemMapper;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.CartItemRepository;
import org.example.repositories.CartRepository;
import org.example.repositories.ProductRepository;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.CartItemService;
import org.example.services.interfaces.DiscountService;
import org.example.services.interfaces.ProductService;
import org.example.services.interfaces.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

@Service
public class CartItemServiceImpl  implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;
    private final DiscountService discountService;
    private final PromotionService promotionService;
    private final SecurityUtils securityUtils;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository,
                               ProductRepository productRepository, CartItemMapper cartItemMapper,
                               DiscountService discountService, PromotionService promotionService,
                               SecurityUtils securityUtils) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemMapper = cartItemMapper;
        this.discountService = discountService;
        this.promotionService = promotionService;
        this.securityUtils = securityUtils;
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    @Override
    public CartItemResponseDto addItemToCart(Long cartId, CartItemDto cartItemDto) {
        User user = getCurrentUser();
        Optional.ofNullable(cartItemDto.getQuantity())
                .filter(quantity -> quantity > 0)
                .orElseThrow(() -> new InvalidQuantityException(ErrorMessage.INVALID_QUANTITY));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        BigDecimal discountPrice = discountService.getCurrentDiscountForProduct(product.getId())
                .map(DiscountDto::getDiscountPrice)
                .orElse(BigDecimal.ZERO);

        BigDecimal promotionDiscount = promotionService.getPromotionDiscountForProduct(product.getId());

        BigDecimal finalPrice = calculateFinalPrice(product.getPrice(), discountPrice, promotionDiscount);

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setDeleted(false);
                    return newItem;
                });

        if (cartItem.getId() != null && cartItem.isDeleted()) {
            cartItem.setDeleted(false);
            cartItem.setQuantity(cartItemDto.getQuantity());
        } else {
            int existingQuantity = cartItem.getQuantity() != null ? cartItem.getQuantity() : 0;
            cartItem.setQuantity(existingQuantity + cartItemDto.getQuantity());
        }
        cartItem.setPrice(finalPrice);
        cartItem.setDiscountPrice(discountPrice);

        cartItemRepository.save(cartItem);

        updateTotalPrice(cart);

        return cartItemMapper.toResponseDto(cartItem);
    }


    private BigDecimal calculateFinalPrice(BigDecimal itemPrice, BigDecimal discountPrice, BigDecimal promotionDiscount) {
        BigDecimal discountedPrice = itemPrice.subtract(discountPrice);

        if (promotionDiscount.compareTo(BigDecimal.ONE) <= 0) {
            discountedPrice = discountedPrice.subtract(discountedPrice.multiply(promotionDiscount));
        } else {
            discountedPrice = discountedPrice.subtract(promotionDiscount);
        }

        return discountedPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPrice;
    }


    @Override
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId, Integer quantity) {
        User user = getCurrentUser();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        if (cartItem.isDeleted()) {
            throw new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND);
        }

        cartItem.setQuantity(quantity);

        Product product = cartItem.getProduct();

        BigDecimal discountPrice = discountService.getCurrentDiscountForProduct(product.getId())
                .map(DiscountDto::getDiscountPrice)
                .orElse(BigDecimal.ZERO);
        BigDecimal promotionDiscount = promotionService.getPromotionDiscountForProduct(product.getId());
        BigDecimal finalPrice = calculateFinalPrice(product.getPrice(), discountPrice, promotionDiscount);
        cartItem.setPrice(finalPrice);
        cartItem.setDiscountPrice(discountPrice);
        cartItemRepository.save(cartItem);
        updateTotalPrice(cartItem.getCart());
        return cartItemMapper.toResponseDto(cartItem);
    }


    @Override
    public void removeCartItem(Long cartItemId) {
        User user = getCurrentUser();

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        cartItem.setDeleted(true);
        cartItemRepository.save(cartItem);

        updateTotalPrice(cartItem.getCart());
    }

    private void updateTotalPrice(Cart cart) {
        BigDecimal newTotalPrice = cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDeleted())
                .map(cartItem -> {
                    BigDecimal itemTotalPrice = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                    BigDecimal itemDiscount = cartItem.getDiscountPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                    return itemTotalPrice.subtract(itemDiscount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(newTotalPrice.max(BigDecimal.ZERO));
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

}
