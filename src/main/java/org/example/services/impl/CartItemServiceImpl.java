package org.example.services.impl;

import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.exception.*;
import org.example.exception.errorMessage.ErrorMessage;
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
    private final UserRepository userRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository,
                               ProductRepository productRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartItemResponseDto addItemToCart(Long cartId, CartItemDto cartItemDto) {
        // Проверка, что количество товара больше нуля
        Optional.ofNullable(cartItemDto.getQuantity())
                .filter(quantity -> quantity > 0)
                .orElseThrow(() -> new InvalidQuantityException(ErrorMessage.INVALID_QUANTITY));

        // Поиск корзины по её ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        // Поиск товара по его ID
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        // Поиск товара в корзине, включая "удалённые" записи
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> new CartItem());

        if (cartItem.getId() != null && cartItem.isDeleted()) {
            // Если товар был ранее добавлен, но "удалён", восстановим его
            cartItem.setDeleted(false);
            cartItem.setQuantity(cartItemDto.getQuantity());
        } else if (cartItem.getId() != null) {
            // Если товар уже в корзине и не удалён, увеличиваем количество
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
        } else {
            // Если это новый товар, устанавливаем новое количество
            cartItem.setQuantity(cartItemDto.getQuantity());
        }

        // Устанавливаем параметры товара в корзине
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setPrice(calculateFinalPrice(cartItem, cartItemDto.getDiscountPrice()));
        cartItem.setDeleted(false);

        // Сохраняем товар в корзине
        cartItemRepository.save(cartItem);

        // Обновляем общую стоимость корзины
        updateTotalPrice(cart);

        // Возвращаем DTO с информацией о добавленном товаре
        return new CartItemResponseDto(
                cartItem.getProduct().getId(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartItem.getDiscountPrice(),
                cartItem.getProduct().getName()
        );
    }
    // Метод для вычисления итоговой цены товара с учетом скидки
    private BigDecimal calculateFinalPrice(CartItem cartItem, BigDecimal discountPrice) {
        BigDecimal itemPrice = cartItem.getProduct().getPrice();// Получаем цену товара
        BigDecimal finalDiscountPrice = discountPrice != null ? discountPrice : BigDecimal.ZERO;// Если скидка есть, используем её, если нет — 0
        // Умножаем цену на количество
        return itemPrice.subtract(finalDiscountPrice).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    // Метод для обновления количества товара в корзине
    @Override
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId, Integer quantity) {
        // Поиск товара в корзине по ID
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND));
        // Проверка, что товар не удален
        if (cartItem.isDeleted()) {
            throw new ProductNotFoundException("Product not found in cart."); //переделаь
        }
        // Обновление количества товара
        cartItem.setQuantity(quantity);
        // Пересчитываем цену с учетом нового количества
        cartItem.setPrice(calculateFinalPrice(cartItem, cartItem.getDiscountPrice()));
        // Сохраняем изменения
        cartItemRepository.save(cartItem);
        // Обновляем общую стоимость корзины
        updateTotalPrice(cartItem.getCart());
// Возвращаем DTO с обновленными данными
        return new CartItemResponseDto(
                cartItem.getProduct().getId(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartItem.getDiscountPrice(),
                cartItem.getProduct().getName()
        );
    }
    // Метод для удаления товара из корзины
    @Override
    public void removeCartItem(Long cartItemId) {
        // Поиск товара в корзине по ID
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND));

        // Устанавливаем статус товара как удаленный
        cartItem.setDeleted(true);

        // Сохраняем изменения
        cartItemRepository.save(cartItem);

        // Обновляем общую стоимость корзины
        updateTotalPrice(cartItem.getCart());
    }
    // Метод для обновления общей стоимости корзины
    private void updateTotalPrice(Cart cart) {
        // Пересчитываем общую стоимость, исключая удалённые товары
        BigDecimal newTotalPrice = cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDeleted())  // Исключаем удалённые товары
                .map(cartItem -> cartItem.getPrice())  // Получаем цену товара
                .reduce(BigDecimal.ZERO, BigDecimal::add);  // Складываем все цены

        // Устанавливаем новую общую стоимость корзины
        cart.setTotalPrice(newTotalPrice);
        cartRepository.save(cart);
    }
}
