package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.dto.OrderCreateDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.Role;
import org.example.models.Cart;
import org.example.models.CartItem;
import org.example.models.Product;
import org.example.models.User;
import org.example.repositories.CartItemRepository;
import org.example.repositories.CartRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.UserRepository;
import org.example.services.impl.JwtSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtSecurityService jwtSecurityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private User user;
    private Cart cart;


@BeforeEach
public void setUp() {
    // Удаляем все данные
    cartItemRepository.deleteAll();
    cartRepository.deleteAll();
    productRepository.deleteAll();
    userRepository.deleteAll();

    // Создаем и сохраняем пользователя
    user = new User();
    user.setEmail("user@example.com");
    user.setPassword(passwordEncoder.encode("password123"));
    user.setRole(Role.USER);
    user.setName("Test User");
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user = userRepository.save(user); // Сохраняем пользователя

    // Создаем и сохраняем корзину
    cart = new Cart();
    cart.setUser(user); // Устанавливаем связь с пользователем
    cart.setCreatedAt(LocalDateTime.now());
    cart.setUpdatedAt(LocalDateTime.now());
    cart.setTotalPrice(BigDecimal.ZERO);

    // Сохраняем корзину
    cart = cartRepository.save(cart); // Сохраняем корзину

    // Устанавливаем корзину у пользователя и сохраняем пользователя
    user.setCart(cart);
    userRepository.save(user);

    // Генерируем токен
    userToken = jwtSecurityService.generateToken(
            org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build()
    );
}


    @Test
    public void testCalculateTotalPrice() throws Exception {
        mockMvc.perform(get("/api/cart/{cartId}/total", cart.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("0"));
    }

    @Test
    public void testConvertCartToOrder() throws Exception {
        // Создаем продукт и сохраняем его
        Product product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product = productRepository.save(product);

        // Создаем и сохраняем элемент корзины
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setPrice(product.getPrice());
        cartItem.setDeleted(false);
        cartItem = cartItemRepository.save(cartItem);

        // Инициализируем коллекцию cartItems, если она null
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        // Добавляем cartItem в коллекцию cartItems корзины
        cart.getCartItems().add(cartItem);

        // Обновляем общую стоимость корзины
        cart.setTotalPrice(product.getPrice());
        cartRepository.save(cart);

        // Создаем OrderCreateDto
        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setDeliveryMethod(DeliveryMethod.COURIER);
        orderCreateDto.setDeliveryAddress("123 Street");
        orderCreateDto.setContactInfo("Contact Info");

        mockMvc.perform(post("/api/cart/convert/{cartId}", cart.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalAmount").value(100.0))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }



    @Test
    public void testClearCart() throws Exception {
        mockMvc.perform(delete("/api/cart/{cartId}/clear", cart.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCartUserRelationship() {
        // Извлекаем корзину из базы данных
        Cart retrievedCart = cartRepository.findById(cart.getId()).orElse(null);
        assertNotNull(retrievedCart, "Корзина не должна быть null");

        // Проверяем, что у корзины установлен пользователь
        assertNotNull(retrievedCart.getUser(), "У корзины должен быть установлен пользователь");
        assertEquals(user.getId(), retrievedCart.getUser().getId(), "ID пользователя в корзине должен совпадать");

        // Извлекаем пользователя из базы данных
        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(retrievedUser, "Пользователь не должен быть null");

        // Проверяем, что у пользователя установлена корзина
        assertNotNull(retrievedUser.getCart(), "У пользователя должна быть установлена корзина");
        assertEquals(cart.getId(), retrievedUser.getCart().getId(), "ID корзины у пользователя должен совпадать");
    }
}
