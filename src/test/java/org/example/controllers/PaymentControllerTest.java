package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;
import org.example.enums.PaymentStatus;
import org.example.enums.Role;
import org.example.exceptions.PaymentNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.PaymentService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PaymentController paymentController;

    private ObjectMapper objectMapper;

    private PaymentRequestDto paymentRequestDto;
    private PaymentResponseDto paymentResponseDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setOrderId(1L);
        paymentRequestDto.setAmount(new BigDecimal("100.00"));


        paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(1L);
        paymentResponseDto.setOrderId(1L);
        paymentResponseDto.setAmount(new BigDecimal("100.00"));
        paymentResponseDto.setStatus(PaymentStatus.PENDING);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRole(Role.USER);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testCreatePayment_Success() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(paymentService.createPayment(any(PaymentRequestDto.class), eq(user.getId()))).thenReturn(paymentResponseDto);
        doReturn("test@example.com").when(SecurityUtils.class);
        SecurityUtils.getCurrentUserEmail();

        String paymentJson = objectMapper.writeValueAsString(paymentRequestDto);

        mockMvc.perform(post("/api/payment")
                        .contentType(APPLICATION_JSON)
                        .content(paymentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(paymentResponseDto.getId().intValue())))
                .andExpect(jsonPath("$.orderId", is(paymentResponseDto.getOrderId().intValue())))
                .andExpect(jsonPath("$.amount", is(paymentResponseDto.getAmount().intValue())))
                .andExpect(jsonPath("$.status", is(paymentResponseDto.getStatus().name())));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testGetPaymentStatus_Success() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(paymentService.getPaymentStatus(1L, user.getId())).thenReturn(paymentResponseDto);
        doReturn("test@example.com").when(SecurityUtils.class);
        SecurityUtils.getCurrentUserEmail();

        mockMvc.perform(get("/api/payment/{paymentId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(paymentResponseDto.getId().intValue())))
                .andExpect(jsonPath("$.orderId", is(paymentResponseDto.getOrderId().intValue())))
                .andExpect(jsonPath("$.amount", is(paymentResponseDto.getAmount().intValue())))
                .andExpect(jsonPath("$.status", is(paymentResponseDto.getStatus().name())));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testUpdatePaymentStatus_Success() throws Exception {
        doNothing().when(paymentService).updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        mockMvc.perform(put("/api/payment/{paymentId}", 1)
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void testUpdatePaymentStatus_NotFound() throws Exception {
        doThrow(new PaymentNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND))
                .when(paymentService).updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        mockMvc.perform(put("/api/payment/{paymentId}", 1)
                        .param("status", "COMPLETED"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorMessage.PAYMENT_NOT_FOUND));
    }
}
