package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.CartDto;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.exceptions.CartAlreadyExistsException;
import org.example.exceptions.CartEmptyException;
import org.example.exceptions.CartNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

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
    }

    @Test
    void testCreateCart_Success() throws Exception {
        when(cartService.createCart(1L)).thenReturn(cartDto);

        mockMvc.perform(post("/api/users/1/cart"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(cartDto.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(cartDto.getUserId().intValue())))
                .andExpect(jsonPath("$.totalPrice", is(0)));
    }

    @Test
    void testCreateCart_AlreadyExists() throws Exception {
        when(cartService.createCart(1L)).thenThrow(new CartAlreadyExistsException(ErrorMessage.CART_ALREADY_EXISTS));

        mockMvc.perform(post("/api/users/1/cart"))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.CART_ALREADY_EXISTS));
    }

    @Test
    void testConvertCartToOrder_Success() throws Exception {
        when(cartService.convertCartToOrder(eq(1L), eq(1L), any(OrderCreateDto.class))).thenReturn(orderDto);

        String orderJson = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/api/users/1/cart/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(jsonPath("$.userId", is(orderDto.getUserId().intValue())))
                .andExpect(jsonPath("$.status", is(orderDto.getStatus().name())));
    }

    @Test
    void testConvertCartToOrder_CartNotFound() throws Exception {
        when(cartService.convertCartToOrder(eq(1L), eq(1L), any(OrderCreateDto.class)))
                .thenThrow(new CartNotFoundException(ErrorMessage.CART_NOT_FOUND));

        String orderJson = objectMapper.writeValueAsString(orderCreateDto);

        mockMvc.perform(post("/api/users/1/cart/convert")
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

        mockMvc.perform(post("/api/users/1/cart/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ErrorMessage.CART_EMPTY));
    }

}
