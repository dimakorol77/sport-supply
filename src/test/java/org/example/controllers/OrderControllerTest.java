package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.OrderDto;
import org.example.dto.OrderResponseDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.enums.Role;
import org.example.models.User;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.OrderService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // Импорт найден
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    private OrderDto orderDto;
    private OrderResponseDto orderResponseDto;
    private User user;

    @BeforeEach
    void setUp() {
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

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", is(orderDto.getUserId().intValue())))
                .andExpect(jsonPath("$[0].status", is(orderDto.getStatus().name())));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetOrdersByUserId_AsUser() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(orderService.getOrdersByUserId(1L)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", is(orderDto.getUserId().intValue())))
                .andExpect(jsonPath("$[0].status", is(orderDto.getStatus().name())));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetOrdersByUserId_AsAdmin_WithUserId() throws Exception {
        User adminUser = new User();
        adminUser.setId(2L);
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.ADMIN);

        when(userService.getUserByEmail("admin@example.com")).thenReturn(adminUser);
        when(orderService.getOrdersByUserId(1L)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/user")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetOrderById_Success() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(orderService.getOrderByIdAndCheckOwnership(1L, 1L)).thenReturn(orderDto);

        mockMvc.perform(get("/api/orders/{orderId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(orderDto.getUserId().intValue())))
                .andExpect(jsonPath("$.status", is(orderDto.getStatus().name())));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testCancelOrder_Success() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        doNothing().when(orderService).cancelOrderAndCheckOwnership(1L, 1L);

        mockMvc.perform(delete("/api/orders/{orderId}/cancel", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testUpdateOrderStatus_Success() throws Exception {
        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.SHIPPED))).thenReturn(orderDto);

        mockMvc.perform(put("/api/orders/{orderId}/status", 1)
                        .param("status", "SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderResponseDto.getId().intValue())))
                .andExpect(jsonPath("$.totalAmount", is(orderResponseDto.getTotalAmount().intValue())))
                .andExpect(jsonPath("$.status", is(orderResponseDto.getStatus().name())));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetOrdersByStatus_Success() throws Exception {
        when(orderService.getOrdersByStatus(OrderStatus.CREATED)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/status")
                        .param("status", "CREATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetOrdersCreatedAfter_Success() throws Exception {
        LocalDateTime date = LocalDateTime.now().minusDays(1);
        when(orderService.getOrdersCreatedAfter(any(LocalDateTime.class))).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/created-after")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testGetOrdersByDeliveryMethod_Success() throws Exception {
        when(orderService.getOrdersByDeliveryMethod(DeliveryMethod.PICKUP)).thenReturn(Collections.singletonList(orderDto));

        mockMvc.perform(get("/api/orders/delivery-method")
                        .param("deliveryMethod", "STANDARD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(orderDto.getId().intValue())));
    }
}
