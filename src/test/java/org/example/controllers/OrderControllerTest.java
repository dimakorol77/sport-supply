package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.enums.Role;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.models.User;
import org.example.repositories.OrderItemRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.impl.JwtSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtSecurityService jwtSecurityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String adminToken;
    private User user;
    private User adminUser;
    private Order order;

    @BeforeEach
    public void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();

        userRepository.findByEmail("user@example.com").ifPresent(userRepository::delete);
        userRepository.findByEmail("admin@example.com").ifPresent(userRepository::delete);

        user = new User();
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("userpass"));
        user.setRole(Role.USER);
        user.setName("Test User");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("adminpass"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setName("Admin User");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(adminUser);

        order = new Order();
        order.setUser(user);
        order.setTotalAmount(BigDecimal.valueOf(100.0));
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryMethod(DeliveryMethod.PICKUP);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        userToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build());

        adminToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(adminUser.getEmail())
                        .password(adminUser.getPassword())
                        .roles(adminUser.getRole().name())
                        .build());
    }

    @Test
    public void testGetOrdersByUser() throws Exception {

        // Создаём уникального пользователя
        String uniqueEmail = "user" + UUID.randomUUID() + "@example.com";
        User regularUser = new User();
        regularUser.setEmail(uniqueEmail);
        regularUser.setPassword(passwordEncoder.encode("userpass"));
        regularUser.setRole(Role.USER);
        regularUser.setName("Regular User");
        regularUser.setCreatedAt(LocalDateTime.now());
        regularUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(regularUser);

        // Генерируем токен для пользователя
        String userToken = jwtSecurityService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(regularUser.getEmail())
                        .password(regularUser.getPassword())
                        .roles(regularUser.getRole().name())
                        .build()
        );

        // Создаём заказ для пользователя regularUser
        Order order = new Order();
        order.setUser(regularUser); // Привязываем заказ к правильному пользователю
        order.setTotalAmount(BigDecimal.valueOf(100.0));
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryMethod(DeliveryMethod.PICKUP); // Добавлено значение
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);



        // Выполняем запрос
        mockMvc.perform(get("/api/orders/user")
                        .header("Authorization", "Bearer " + userToken)
                        .param("userId", regularUser.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(order.getId().intValue())))
                .andExpect(jsonPath("$[0].totalAmount", is(100.0)));
    }



    @Test
    public void testUpdateOrderStatusByAdmin() throws Exception {
        mockMvc.perform(put("/api/orders/{id}/status", order.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PAID")));
    }

    @Test
    public void testCancelOrderByUser() throws Exception {
        mockMvc.perform(delete("/api/orders/{id}/cancel", order.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNoContent());

        Order updatedOrder = orderRepository.findById(order.getId()).get();
        assertThat(updatedOrder.getStatus(), is(OrderStatus.CANCELLED));
    }

    @Test
    public void testGetAllOrdersByAdmin() throws Exception {
        mockMvc.perform(get("/api/orders/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(order.getId().intValue())))
                .andExpect(jsonPath("$[0].totalAmount", is(100.0)));

    }

    @Test
    public void testGetOrderByIdAndCheckOwnership() throws Exception {
        mockMvc.perform(get("/api/orders/{id}", order.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId().intValue())))
                .andExpect(jsonPath("$.totalAmount", is(100.0)));
    }
    @Test
    @Transactional
    public void testGetOrdersByStatus() throws Exception {
        Order order = orderRepository.findById(this.order.getId()).orElseThrow();
        order.setStatus(OrderStatus.SHIPPED);


        if (order.getOrderItems() == null) {
            order.setOrderItems(new ArrayList<>());
        }


        order.getOrderItems().clear();
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductName("Test Product");
        item.setPrice(BigDecimal.valueOf(50.0));
        item.setQuantity(2);
        order.getOrderItems().add(item);

        orderRepository.save(order);

        mockMvc.perform(get("/api/orders/status")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("status", "SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(order.getId().intValue())))
                .andExpect(jsonPath("$[0].status", is("SHIPPED")));
    }


    @Test
    public void testGetOrdersCreatedAfter() throws Exception {
        LocalDateTime filterDate = LocalDateTime.now().minusDays(1);
        String formattedDate = filterDate.format(DateTimeFormatter.ISO_DATE_TIME);


        mockMvc.perform(get("/api/orders/created-after")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("date", formattedDate)) // Дата в формате ISO 8601
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(order.getId().intValue())))
                .andExpect(jsonPath("$[0].createdAt", notNullValue()));
    }



    @Test
    public void testGetOrdersByDeliveryMethod() throws Exception {
        mockMvc.perform(get("/api/orders/delivery-method")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("deliveryMethod", "PICKUP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(order.getId().intValue())))
                .andExpect(jsonPath("$[0].deliveryMethod", is("PICKUP")));
    }

}
