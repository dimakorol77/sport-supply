package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.OrderItemCreateDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    private String ownerToken;
    private String otherUserToken;
    private User orderOwner;
    private User otherUser;
    private Order order;

    @BeforeEach
    public void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();

        userRepository.findByEmail("user@example.com").ifPresent(userRepository::delete);
        userRepository.findByEmail("admin@example.com").ifPresent(userRepository::delete);

        orderOwner = new User();
        orderOwner.setEmail("owner@example.com");
        orderOwner.setPassword(passwordEncoder.encode("ownerpass"));
        orderOwner.setRole(Role.USER);
        orderOwner.setName("Order Owner");
        orderOwner.setCreatedAt(LocalDateTime.now());
        orderOwner.setUpdatedAt(LocalDateTime.now());
        userRepository.save(orderOwner);


        otherUser = new User();
        otherUser.setEmail("other@example.com");
        otherUser.setPassword(passwordEncoder.encode("otherpass"));
        otherUser.setRole(Role.USER);
        otherUser.setName("Other User");
        otherUser.setCreatedAt(LocalDateTime.now());
        otherUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(otherUser);


        order = new Order();
        order.setUser(orderOwner);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryMethod(DeliveryMethod.PICKUP);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);


        ownerToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(orderOwner.getEmail())
                        .password(orderOwner.getPassword())
                        .roles(orderOwner.getRole().name())
                        .build());

        otherUserToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(otherUser.getEmail())
                        .password(otherUser.getPassword())
                        .roles(otherUser.getRole().name())
                        .build());
    }

    @Test
    public void testCreateOrderItemByOwner() throws Exception {
        Product product = createProduct("Product1", "Description1", BigDecimal.valueOf(100.0));

        OrderItemCreateDto orderItemCreateDto = new OrderItemCreateDto();
        orderItemCreateDto.setProductId(product.getId());
        orderItemCreateDto.setQuantity(2);
        orderItemCreateDto.setPrice(BigDecimal.valueOf(100.0));

        mockMvc.perform(post("/api/order-items/{orderId}", order.getId())
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.price", is(100.0)));
    }

    @Test
    public void testCreateOrderItemByOtherUserForbidden() throws Exception {
        Product product = createProduct("Product2", "Description2", BigDecimal.valueOf(50.0));

        OrderItemCreateDto orderItemCreateDto = new OrderItemCreateDto();
        orderItemCreateDto.setProductId(product.getId());
        orderItemCreateDto.setQuantity(1);
        orderItemCreateDto.setPrice(BigDecimal.valueOf(50.0)); // Установка значения

        mockMvc.perform(post("/api/order-items/{orderId}", order.getId())
                        .header("Authorization", "Bearer " + otherUserToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemCreateDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetOrderItemsByOrderIdByOwner() throws Exception {
        Product product = createProduct("Product3", "Description3", BigDecimal.valueOf(70.0));
        createOrderItem(product);

        mockMvc.perform(get("/api/order-items/{orderId}", order.getId())
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId", is(product.getId().intValue())))
                .andExpect(jsonPath("$[0].quantity", is(1)))
                .andExpect(jsonPath("$[0].price", is(70.0)));
    }

    @Test
    public void testGetOrderItemsByOrderIdByOtherUserForbidden() throws Exception {
        mockMvc.perform(get("/api/order-items/{orderId}", order.getId())
                        .header("Authorization", "Bearer " + otherUserToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void testDeleteOrderItemByOwner() throws Exception {
        Product product = createProduct("Product4", "Description4", BigDecimal.valueOf(80.0));
        OrderItem orderItem = createOrderItem(product);

        mockMvc.perform(delete("/api/order-items/{orderItemId}", orderItem.getId())
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isNoContent());


        assertFalse(orderItemRepository.existsById(orderItem.getId()));


        Order order = orderRepository.findById(orderItem.getOrder().getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        assertNotNull(order.getOrderItems());
        assertFalse(order.getOrderItems().contains(orderItem));
    }






    private Product createProduct(String name, String description, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // Setting and saving the category
        Category category = new Category();
        category.setName("Default Category");
        category.setDescription("Default Description");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category = categoryRepository.save(category);
        product.setCategory(category);

        productRepository.save(product);
        return product;
    }



    private OrderItem createOrderItem(Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(1);
        orderItemRepository.save(orderItem);
        return orderItem;
    }
}
