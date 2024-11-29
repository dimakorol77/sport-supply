package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CartItemDto;
import org.example.dto.CartItemResponseDto;
import org.example.enums.Role;
import org.example.models.*;
import org.example.repositories.*;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

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
        cartItemRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем пользователя
        user = new User();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        user.setName("Test User");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Создаем корзину для пользователя
        cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        // Генерируем JWT токен
        userToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build());
    }

    @Test
    public void testAddItemToCart() throws Exception {
        // Создаем продукт
        Product product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setPrice(BigDecimal.valueOf(50.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        // Создаем DTO для добавления товара в корзину
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(product.getId());
        cartItemDto.setQuantity(2);

        mockMvc.perform(post("/api/cart-items/{cartId}/items", cart.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.price", is(50.0)))
                .andExpect(jsonPath("$.name", is("Product1")));
    }

    @Test
    public void testUpdateCartItemQuantity() throws Exception {
        // Создаем продукт
        Product product = new Product();
        product.setName("Product2");
        product.setDescription("Description2");
        product.setPrice(BigDecimal.valueOf(30.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        // Создаем элемент корзины
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setPrice(product.getPrice());
        cartItem.setDeleted(false);
        cartItemRepository.save(cartItem);

        // Обновляем количество товара в корзине
        mockMvc.perform(put("/api/cart-items/{cartItemId}/quantity", cartItem.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(5)));
    }

    @Test
    public void testRemoveCartItem() throws Exception {
        // Создаем продукт
        Product product = new Product();
        product.setName("Product3");
        product.setDescription("Description3");
        product.setPrice(BigDecimal.valueOf(20.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        // Создаем элемент корзины
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setPrice(product.getPrice());
        cartItem.setDeleted(false);
        cartItemRepository.save(cartItem);

        // Удаляем элемент из корзины
        mockMvc.perform(delete("/api/cart-items/{cartItemId}", cartItem.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());
    }
}
