package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

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

    private String adminToken;
    private User adminUser;
    private Order order;

    @BeforeEach
    public void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем администратора
        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setName("Admin User");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        // Создаем заказ
        order = new Order();
        order.setUser(adminUser);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryMethod(DeliveryMethod.PICKUP); // Пример значения
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // Генерируем JWT токен для администратора
        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build());
    }

    @Test
    public void testCreateOrderItem() throws Exception {
        // Создаем продукт
        Product product = new Product();
        product.setName("Product1");
        product.setDescription("Description1");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        // Создаем DTO для создания элемента заказа
        OrderItemCreateDto orderItemCreateDto = new OrderItemCreateDto();
        orderItemCreateDto.setProductId(product.getId());
        orderItemCreateDto.setQuantity(2);
        orderItemCreateDto.setPrice(BigDecimal.valueOf(100.0)); // Установка цены

        // Выполняем запрос
        mockMvc.perform(post("/api/order-items/{orderId}/items", order.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId", is(product.getId().intValue())))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.price", is(100.0)));
    }




    @Test
    public void testGetOrderItemsByOrderId() throws Exception {
        // Создаем продукт
        Product product = new Product();
        product.setName("Product2");
        product.setDescription("Description2");
        product.setPrice(BigDecimal.valueOf(50.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        // Создаем элемент заказа
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(1);
        orderItemRepository.save(orderItem);

        mockMvc.perform(get("/api/order-items/{orderId}", order.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId", is(product.getId().intValue())))
                .andExpect(jsonPath("$[0].quantity", is(1)))
                .andExpect(jsonPath("$[0].price", is(50.0)));
    }

    @Test
    public void testUpdateOrderItem() throws Exception {
        // Создаем продукт
        Product product = new Product();
        product.setName("Product3");
        product.setDescription("Description3");
        product.setPrice(BigDecimal.valueOf(70.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        // Создаем элемент заказа
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(1);
        orderItemRepository.save(orderItem);

        // Создаем DTO для обновления элемента заказа
        OrderItemCreateDto orderItemCreateDto = new OrderItemCreateDto();
        orderItemCreateDto.setProductId(product.getId());
        orderItemCreateDto.setQuantity(3);

        mockMvc.perform(put("/api/order-items/{orderItemId}", orderItem.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItemCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(3)));
    }

    @Test
    public void testDeleteOrderItem() throws Exception {
        // Создаем продукт
        Product product = new Product();
        product.setName("Product4");
        product.setDescription("Description4");
        product.setPrice(BigDecimal.valueOf(80.0));
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);

        // Создаем элемент заказа
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(1);
        orderItemRepository.save(orderItem);

        mockMvc.perform(delete("/api/order-items/{orderItemId}", orderItem.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
