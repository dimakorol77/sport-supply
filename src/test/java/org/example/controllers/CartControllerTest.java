package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.exceptions.CartEmptyException;
import org.example.exceptions.CartNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartController cartController;

    private CartDto cartDto;
    private OrderCreateDto orderCreateDto;
    private OrderDto orderDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setUserId(1L);
        cartDto.setCreatedAt(LocalDateTime.now());
        cartDto.setTotalPrice(new BigDecimal("0.00"));

        orderCreateDto = new OrderCreateDto();
        orderCreateDto.setDeliveryMethod(DeliveryMethod.COURIER);
        orderCreateDto.setDeliveryAddress("123 Street");
        orderCreateDto.setContactInfo("user@example.com");

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);
        orderDto.setStatus(OrderStatus.CREATED);

        // Устанавливаем SecurityContext с аутентифицированным пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("user@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Мокаем метод userService.getUserByEmail()
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);
    }

    @Test
    void testConvertCartToOrder_Success() throws Exception {
        when(cartService.convertCartToOrder(eq(1L), eq(1L), any(OrderCreateDto.class))).thenReturn(orderDto);

        String orderJson = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/api/cart/convert/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.status", is(orderDto.getStatus().name())));
    }

    @Test
    void testConvertCartToOrder_CartNotFound() throws Exception {
        when(cartService.convertCartToOrder(eq(1L), eq(1L), any(OrderCreateDto.class)))
                .thenThrow(new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        String orderJson = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/api/cart/convert/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.CART_NOT_FOUND));
    }

    @Test
    void testConvertCartToOrder_EmptyCart() throws Exception {
        when(cartService.convertCartToOrder(eq(1L), eq(1L), any(OrderCreateDto.class)))
                .thenThrow(new CartEmptyException(ErrorMessage.CART_EMPTY));

        String orderJson = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/api/cart/convert/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ErrorMessage.CART_EMPTY));
    }

    @Test
    void testCalculateTotalPrice_Success() throws Exception {
        when(cartService.calculateTotalPrice(eq(1L), eq(1L))).thenReturn(new BigDecimal("100.00"));

        mockMvc.perform(get("/api/cart/1/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.00"));
    }

//    @Test
//    void testClearCart_Success() throws Exception {
//        doNothing().when(cartService).clearCart(eq(1L), eq(1L));
//
//        mockMvc.perform(delete("/api/cart/1/clear"))
//                .andExpect(status().isNoContent());
//    }


}
