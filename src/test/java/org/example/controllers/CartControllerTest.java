package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CartItemDto;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

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
        cartRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();

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
        cart = new Cart(); // Здесь инициализируем объект
        cart.setUser(user); // Устанавливаем связь с пользователем
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setTotalPrice(BigDecimal.ZERO);
        cart = cartRepository.saveAndFlush(cart); // Сохраняем корзину

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
    public void testAddItemToCart() throws Exception {
        Product product = new Product();
        product.setName("Product1");
        product.setDescription("Description");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(product.getId());
        cartItemDto.setQuantity(2);

        mockMvc.perform(post("/api/carts/{id}/items", cart.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.price", is(100.0)))
                .andExpect(jsonPath("$.name", is("Product1")));
    }

    @Test
    public void testCalculateTotalPrice() throws Exception {
        Product product1 = new Product();
        product1.setName("Product1");
        product1.setDescription("Description1");
        product1.setPrice(BigDecimal.valueOf(100.0));
        product1.setCreatedAt(LocalDateTime.now());
        product1.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product1);

        CartItem cartItem1 = new CartItem();
        cartItem1.setCart(cart);
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(2);
        cartItem1.setPrice(product1.getPrice());
        cartItem1.setDeleted(false);
        cartItemRepository.save(cartItem1);

        Product product2 = new Product();
        product2.setName("Product2");
        product2.setDescription("Description2");
        product2.setPrice(BigDecimal.valueOf(50.0));
        product2.setCreatedAt(LocalDateTime.now());
        product2.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product2);

        CartItem cartItem2 = new CartItem();
        cartItem2.setCart(cart);
        cartItem2.setProduct(product2);
        cartItem2.setQuantity(1);
        cartItem2.setPrice(product2.getPrice());
        cartItem2.setDeleted(false);
        cartItemRepository.save(cartItem2);

        mockMvc.perform(get("/api/carts/{id}/total-price", cart.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().string("250.0"));
    }

    @Test
    public void testConvertCartToOrder() throws Exception {
        Product product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setPrice(product.getPrice());
        cartItem.setDeleted(false);
        cartItemRepository.save(cartItem);

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setDeliveryMethod(DeliveryMethod.COURIER);
        orderCreateDto.setDeliveryAddress("123 Street");
        orderCreateDto.setContactInfo("Contact Info");

        mockMvc.perform(post("/api/carts/{id}/convert-to-order", cart.getId())
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$.deliveryMethod", is("HOME_DELIVERY")))
                .andExpect(jsonPath("$.totalAmount", is(100.0)));
    }
}
