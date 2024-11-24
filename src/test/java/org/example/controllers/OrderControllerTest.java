package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.OrderDto;
import org.example.dto.OrderResponseDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.enums.Role;
import org.example.mappers.OrderMapper;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser; // Импорт найден
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper;

    private OrderDto orderDto;
    private OrderResponseDto orderResponseDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .build();

        objectMapper = new ObjectMapper();

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);
        orderDto.setStatus(OrderStatus.CREATED);
        orderDto.setTotalAmount(BigDecimal.valueOf(100));

        orderResponseDto = new OrderResponseDto(orderDto.getId(), orderDto.getTotalAmount(), orderDto.getStatus());

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        // Очищаем SecurityContext после каждого теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetAllOrders() throws Exception {
        setAuthentication("admin@example.com", "ADMIN");

        User adminUser = createUser(2L, "admin@example.com", Role.ADMIN);
        when(userService.getUserByEmail("admin@example.com")).thenReturn(adminUser);

        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", is(orderDto.getUserId().intValue())))
                .andExpect(jsonPath("$[0].status", is(orderDto.getStatus().name())));
    }

    @Test
    void testGetOrdersByUserId_AsUser() throws Exception {
        setAuthentication("test@example.com", "USER");

        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(orderService.getOrdersByUserId(1L)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", is(orderDto.getUserId().intValue())))
                .andExpect(jsonPath("$[0].status", is(orderDto.getStatus().name())));
    }

    @Test
    void testGetOrdersByUserId_AsAdmin_WithUserId() throws Exception {
        setAuthentication("admin@example.com", "ADMIN");

        User adminUser = createUser(2L, "admin@example.com", Role.ADMIN);
        when(userService.getUserByEmail("admin@example.com")).thenReturn(adminUser);

        when(orderService.getOrdersByUserId(1L)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/user")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        setAuthentication("test@example.com", "USER");

        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(orderService.getOrderByIdAndCheckOwnership(1L, 1L)).thenReturn(orderDto);

        mockMvc.perform(get("/api/orders/{orderId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(orderDto.getUserId().intValue())))
                .andExpect(jsonPath("$.status", is(orderDto.getStatus().name())));
    }

    @Test
    void testUpdateOrderStatus_Success() throws Exception {
        setAuthentication("admin@example.com", "ADMIN");

        User adminUser = createUser(2L, "admin@example.com", Role.ADMIN);
        when(userService.getUserByEmail("admin@example.com")).thenReturn(adminUser);

        OrderDto updatedOrderDto = new OrderDto();
        updatedOrderDto.setId(1L);
        updatedOrderDto.setUserId(1L);
        updatedOrderDto.setStatus(OrderStatus.SHIPPED);
        updatedOrderDto.setTotalAmount(BigDecimal.valueOf(100));

        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.SHIPPED))).thenReturn(updatedOrderDto);

        mockMvc.perform(put("/api/orders/{orderId}/status", 1)
                        .param("status", "SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedOrderDto.getId().intValue())))
                .andExpect(jsonPath("$.totalAmount", is(updatedOrderDto.getTotalAmount().intValue())))
                .andExpect(jsonPath("$.status", is(updatedOrderDto.getStatus().name())));
    }

    @Test
    void testGetOrdersByStatus_Success() throws Exception {
        setAuthentication("admin@example.com", "ADMIN");

        User adminUser = createUser(2L, "admin@example.com", Role.ADMIN);
        when(userService.getUserByEmail("admin@example.com")).thenReturn(adminUser);

        when(orderService.getOrdersByStatus(OrderStatus.CREATED)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/status")
                        .param("status", "CREATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }

    @Test
    void testGetOrdersCreatedAfter_Success() throws Exception {
        setAuthentication("admin@example.com", "ADMIN");

        User adminUser = createUser(2L, "admin@example.com", Role.ADMIN);
        when(userService.getUserByEmail("admin@example.com")).thenReturn(adminUser);

        LocalDateTime date = LocalDateTime.now().minusDays(1);
        when(orderService.getOrdersCreatedAfter(any(LocalDateTime.class))).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/created-after")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }

    @Test
    void testGetOrdersByDeliveryMethod_Success() throws Exception {
        setAuthentication("admin@example.com", "ADMIN");

        User adminUser = createUser(2L, "admin@example.com", Role.ADMIN);
        when(userService.getUserByEmail("admin@example.com")).thenReturn(adminUser);

        when(orderService.getOrdersByDeliveryMethod(DeliveryMethod.PICKUP)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/delivery-method")
                        .param("deliveryMethod", "PICKUP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }

    private void setAuthentication(String email, String role) {
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User(email, "", Collections.singleton(() -> "ROLE_" + role));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User createUser(Long id, String email, Role role) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setRole(role);
        return user;
    }
}