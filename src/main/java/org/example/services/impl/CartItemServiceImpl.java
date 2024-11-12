package org.example.services.impl;

import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.exception.*;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.CartItemMapper;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.CartItemRepository;
import org.example.repositories.CartRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CartItemServiceImpl  implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository,
                               ProductRepository productRepository, CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemMapper = cartItemMapper;  // Инициализируем маппер
    }

    @Override
    public CartItemResponseDto addItemToCart(Long cartId, CartItemDto cartItemDto) {
        Optional.ofNullable(cartItemDto.getQuantity())
                .filter(quantity -> quantity > 0)
                .orElseThrow(() -> new InvalidQuantityException(ErrorMessage.INVALID_QUANTITY));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(CartItem::new);

        if (cartItem.getId() != null && cartItem.isDeleted()) {
            cartItem.setDeleted(false);
            cartItem.setQuantity(cartItemDto.getQuantity());
        } else if (cartItem.getId() != null) {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
        } else {
            cartItem.setQuantity(cartItemDto.getQuantity());
        }

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setPrice(calculateFinalPrice(cartItem, cartItemDto.getDiscountPrice()));
        cartItem.setDeleted(false);

        cartItemRepository.save(cartItem);

        // Пересчёт общей стоимости корзины после добавления товара
        updateTotalPrice(cart);

        return cartItemMapper.toResponseDto(cartItem);
    }

    // Метод для вычисления итоговой цены товара с учетом скидки
    private BigDecimal calculateFinalPrice(CartItem cartItem, BigDecimal discountPrice) {
        BigDecimal itemPrice = cartItem.getProduct().getPrice();
        BigDecimal finalDiscountPrice = discountPrice != null ? discountPrice : BigDecimal.ZERO;
        BigDecimal finalPrice = itemPrice.subtract(finalDiscountPrice);
        return finalPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalPrice;
    }

    // Метод для обновления количества товара в корзине
    @Override
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND));

        if (cartItem.isDeleted()) {
            throw new ProductNotFoundException("Product not found in cart.");//
        }

        cartItem.setQuantity(quantity);
        cartItem.setPrice(calculateFinalPrice(cartItem, cartItem.getDiscountPrice()));
        cartItemRepository.save(cartItem);

        // Пересчёт общей стоимости корзины после изменения количества товара
        updateTotalPrice(cartItem.getCart());

        return cartItemMapper.toResponseDto(cartItem);
    }
    // Метод для удаления товара из корзины
    @Override
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND));

        cartItem.setDeleted(true);
        cartItemRepository.save(cartItem);

        // Пересчёт общей стоимости корзины после удаления товара
        updateTotalPrice(cartItem.getCart());
    }
    // Метод для обновления общей стоимости корзины
    private void updateTotalPrice(Cart cart) {
        BigDecimal newTotalPrice = cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDeleted())
                .map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(newTotalPrice.max(BigDecimal.ZERO));
        cartRepository.save(cart);
    }
}
