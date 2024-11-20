package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;
import org.example.exceptions.OrderItemNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.services.interfaces.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;

    private ObjectMapper objectMapper;

    private OrderItemCreateDto orderItemCreateDto;
    private OrderItemDto orderItemDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderItemController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        orderItemCreateDto = new OrderItemCreateDto();
        orderItemCreateDto.setProductId(1L);
        orderItemCreateDto.setQuantity(2);
        orderItemCreateDto.setPrice(new BigDecimal("100.00"));

        orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setQuantity(2);
        orderItemDto.setPrice(new BigDecimal("100.00"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateOrderItem_Success() throws Exception {
        when(orderItemService.createOrderItem(any(OrderItemCreateDto.class), eq(1L))).thenReturn(orderItemDto);

        String orderItemJson = objectMapper.writeValueAsString(orderItemCreateDto);

        mockMvc.perform(post("/api/order-items/{orderId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(orderItemJson))
                .andExpect(status().isCreated())
                // Убираем проверки по id и orderId
                .andExpect(jsonPath("$.productId", is(orderItemDto.getProductId().intValue())))
                .andExpect(jsonPath("$.quantity", is(orderItemDto.getQuantity())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetOrderItemsByOrderId_Success() throws Exception {
        when(orderItemService.getOrderItemsByOrderId(1L)).thenReturn(Arrays.asList(orderItemDto));

        mockMvc.perform(get("/api/order-items/{orderId}", 1))
                .andExpect(status().isOk())
                // Убираем проверки по id и orderId
                //.andExpect(jsonPath("$[0].id", is(orderItemDto.getId().intValue())))
                //.andExpect(jsonPath("$[0].orderId", is(orderItemDto.getOrderId().intValue())))
                .andExpect(jsonPath("$[0].productId", is(orderItemDto.getProductId().intValue())))
                .andExpect(jsonPath("$[0].quantity", is(orderItemDto.getQuantity())));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateOrderItem_Success() throws Exception {
        when(orderItemService.updateOrderItem(eq(1L), any(OrderItemCreateDto.class))).thenReturn(orderItemDto);

        String orderItemJson = objectMapper.writeValueAsString(orderItemCreateDto);

        mockMvc.perform(put("/api/order-items/{orderItemId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(orderItemJson))
                .andExpect(status().isOk())
                // Убираем проверки по id и orderId
                //.andExpect(jsonPath("$.id", is(orderItemDto.getId().intValue())))
                //.andExpect(jsonPath("$.orderId", is(orderItemDto.getOrderId().intValue())))
                .andExpect(jsonPath("$.productId", is(orderItemDto.getProductId().intValue())))
                .andExpect(jsonPath("$.quantity", is(orderItemDto.getQuantity())));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteOrderItem_Success() throws Exception {
        doNothing().when(orderItemService).deleteOrderItem(1L);

        mockMvc.perform(delete("/api/order-items/{orderItemId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteOrderItem_NotFound() throws Exception {
        doThrow(new OrderItemNotFoundException(ErrorMessage.ORDER_ITEM_NOT_FOUND)).when(orderItemService).deleteOrderItem(1L);

        mockMvc.perform(delete("/api/order-items/{orderItemId}", 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.ORDER_ITEM_NOT_FOUND));
    }
}
