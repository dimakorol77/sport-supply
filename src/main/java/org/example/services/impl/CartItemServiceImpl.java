package org.example.services.impl;

import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.dto.DiscountDto;
import org.example.exceptions.*;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.CartItemMapper;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Product;
import org.example.repositories.CartItemRepository;
import org.example.repositories.CartRepository;
import org.example.repositories.ProductRepository;
import org.example.services.interfaces.CartItemService;
import org.example.services.interfaces.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartItemServiceImpl  implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;
    private final DiscountService discountService;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository,
                               ProductRepository productRepository, CartItemMapper cartItemMapper,
                               DiscountService discountService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemMapper = cartItemMapper;
        this.discountService = discountService;
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

        // Получаем текущую скидку для продукта
        Optional<DiscountDto> optionalDiscount = discountService.getCurrentDiscountForProduct(product.getId());
        BigDecimal discountPrice = optionalDiscount.map(DiscountDto::getDiscountPrice).orElse(BigDecimal.ZERO);

        // Рассчитываем итоговую цену с учетом скидки
        BigDecimal finalPrice = calculateFinalPrice(product.getPrice(), discountPrice);

        // Находим существующий CartItem или создаем новый
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
        } else if (cartItem.getId() != null) {
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
        } else {
            cartItem.setQuantity(cartItemDto.getQuantity());
        }

        cartItem.setPrice(finalPrice);
        cartItem.setDiscountPrice(discountPrice);

        cartItemRepository.save(cartItem);

        // Пересчитываем общую стоимость корзины после добавления товара
        updateTotalPrice(cart);

        return cartItemMapper.toResponseDto(cartItem);
    }

    // Метод для вычисления итоговой цены товара с учетом скидки
    private BigDecimal calculateFinalPrice(BigDecimal itemPrice, BigDecimal discountPrice) {
        BigDecimal finalPrice = itemPrice.subtract(discountPrice);
        return finalPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : finalPrice;
    }


    // Метод для обновления количества товара в корзине
    @Override
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND));

        if (cartItem.isDeleted()) {
            throw new CartItemNotFoundException(ErrorMessage.CART_ITEM_NOT_FOUND);
        }

        cartItem.setQuantity(quantity);

        // Обновляем скидку и цену на случай, если они изменились
        Product product = cartItem.getProduct();
        Optional<DiscountDto> optionalDiscount = discountService.getCurrentDiscountForProduct(product.getId());
        BigDecimal discountPrice = optionalDiscount.map(DiscountDto::getDiscountPrice).orElse(BigDecimal.ZERO);
        BigDecimal finalPrice = calculateFinalPrice(product.getPrice(), discountPrice);

        cartItem.setPrice(finalPrice);
        cartItem.setDiscountPrice(discountPrice);

        cartItemRepository.save(cartItem);

        // Пересчитываем общую стоимость корзины после изменения количества товара
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

        // Пересчитываем общую стоимость корзины после удаления товара
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
