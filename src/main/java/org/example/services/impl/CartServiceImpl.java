package org.example.services.impl;

import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.enums.OrderStatus;
import org.example.exception.CartNotFoundException;
import org.example.exception.ProductNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
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
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, CartItemRepository cartItemRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    // Создание новой корзины для пользователя? - при регистрации пригодится, при входе
    @Override
    public Cart createCart(Long userId) {
        // Получение пользователя по ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));
// Создание новой корзины
        Cart cart = new Cart();
        cart.setUser(user);// Связываем корзину с пользователем
        cart.setCreatedAt(LocalDateTime.now()); // Устанавливаем время создания
        cart.setTotalPrice(BigDecimal.ZERO);// Начальная стоимость корзины = 0
        // Сохраняем корзину в базе данных
        return cartRepository.save(cart);
    }
    // Метод для подсчёта общей стоимости корзины
    @Override
    public BigDecimal calculateTotalPrice(Long cartId) {
        // Получаем корзину по ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));
// Вычисляем сумму товаров в корзине
        return cart.getCartItems().stream()
                .filter(cartItem -> !cartItem.isDeleted())// Исключаем удалённые товары
                .map(cartItem -> cartItem.getPrice() != null
                        ? cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))// Умножаем цену на количество
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);// Суммируем все элементы
    }
    // Очистка корзины (удаление всех товаров)
    @Override
//    public void clearCart(Long cartId) {
//        // Получаем корзину по ID
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));
//
//        // Обновляем все товары в корзине, устанавливая им статус "удалено"
//        cart.getCartItems().forEach(cartItem -> {
//            cartItem.setDeleted(true);
//            cartItemRepository.save(cartItem);  // Сохраняем изменения для каждого товара
//        });
//
//        // Сбрасываем общую стоимость корзины
//        cart.setTotalPrice(BigDecimal.ZERO);  // Устанавливаем общую стоимость в 0
//        cartRepository.save(cart);  // Сохраняем обновленную корзину
//    }
    public void clearCart(Long cartId) {
        // Получаем корзину по ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        // Обновляем все товары в корзине, устанавливая им статус "удалено"
        cart.getCartItems().forEach(cartItem -> {
            cartItem.setDeleted(true);
            cartItemRepository.save(cartItem);
        });

        // Обновляем общую стоимость корзины
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    // Метод для преобразования Cart в CartDto
    private CartDto mapToCartDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUser().getId());
        cartDto.setCreatedAt(cart.getCreatedAt());
        return cartDto;
    }

    // Метод конвертации корзины в заказ
//    public Order convertCartToOrder(Long cartId, OrderCreateDto orderCreateDto) {
//        // Получаем корзину по ID
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));
//
//        // Создаем новый заказ
//        Order order = new Order();
//        order.setUser(cart.getUser());
//        order.setTotalAmount(cart.getTotalPrice()); // Устанавливаем общую сумму из корзины
//        order.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
//        order.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
//        order.setContactInfo(orderCreateDto.getContactInfo());
//        order.setCreatedAt(LocalDateTime.now());
//        order.setUpdatedAt(LocalDateTime.now());
//        order.setStatus(OrderStatus.CREATED); // Начальный статус
//
//        // Сохраняем заказ в базе данных, чтобы он получил ID
//        order = orderRepository.save(order);
//
//        // Переносим товары из корзины в заказ
//        List<OrderItem> orderItems = cart.getCartItems().stream()
//                .map(cartItem -> {
//                    Product product = productRepository.findById(cartItem.getProduct().getId())
//                            .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));
//
//                    OrderItem orderItem = new OrderItem();
//                    orderItem.setProduct(product);
//                    orderItem.setPrice(cartItem.getPrice());
//                    orderItem.setQuantity(cartItem.getQuantity());
//                    orderItem.setOrder(order); // Устанавливаем связь с заказом
//
//                    return orderItem;
//                })
//                .collect(Collectors.toList());
//
//        // Устанавливаем товары в заказе
//        order.setOrderItems(orderItems);
//
//        // Сохраняем обновленный заказ с товарами
//        orderRepository.save(order);
//
//        // Очистка корзины (удаление всех товаров)
//        clearCart(cartId); // Сбрасываем корзину после создания заказа
//
//        return order;
//    }
}

