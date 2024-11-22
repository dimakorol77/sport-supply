package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.*;
import org.example.enums.PaymentStatus;
import org.example.enums.Role;
import org.example.exceptions.PaymentNotFoundException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.services.interfaces.PaymentService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
// Убедитесь, что вы импортируете правильный класс AccessDeniedException
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

// Импортируем только необходимые методы из Hamcrest
import static org.hamcrest.Matchers.is;
// Импортируем все методы из Mockito ArgumentMatchers
import static org.mockito.ArgumentMatchers.*;
// Импортируем Mockito методы
import static org.mockito.Mockito.*;
// Импортируем методы для MockMvc
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

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
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
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
    void testCreatePayment_Success() throws Exception {
        // Устанавливаем аутентификацию
        setAuthentication("test@example.com", "USER");

        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(paymentService.createPayment(any(PaymentRequestDto.class), eq(user.getId()))).thenReturn(paymentResponseDto);

        String paymentJson = objectMapper.writeValueAsString(paymentRequestDto);

        mockMvc.perform(post("/api/payment/create")
                        .contentType(APPLICATION_JSON)
                        .content(paymentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(paymentResponseDto.getId().intValue())))
                .andExpect(jsonPath("$.orderId", is(paymentResponseDto.getOrderId().intValue())))
                .andExpect(jsonPath("$.amount", is(100.0)))
                .andExpect(jsonPath("$.status", is(paymentResponseDto.getStatus().name())));
    }

    @Test
    void testGetPaymentStatus_Success() throws Exception {
        // Устанавливаем аутентификацию
        setAuthentication("test@example.com", "USER");

        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(paymentService.getPaymentStatus(1L, user.getId())).thenReturn(paymentResponseDto);

        mockMvc.perform(get("/api/payment/{paymentId}/status", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(paymentResponseDto.getId().intValue())))
                .andExpect(jsonPath("$.orderId", is(paymentResponseDto.getOrderId().intValue())))
                .andExpect(jsonPath("$.amount", is(100.0)))
                .andExpect(jsonPath("$.status", is(paymentResponseDto.getStatus().name())));
    }

    @Test
    void testUpdatePaymentStatus_Success() throws Exception {
        // Устанавливаем аутентификацию
        setAuthentication("admin@example.com", "ADMIN");

        doNothing().when(paymentService).updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        mockMvc.perform(patch("/api/payment/{paymentId}/status", 1)
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatePaymentStatus_NotFound() throws Exception {
        // Устанавливаем аутентификацию
        setAuthentication("admin@example.com", "ADMIN");

        doThrow(new PaymentNotFoundException(ErrorMessage.PAYMENT_NOT_FOUND))
                .when(paymentService).updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        mockMvc.perform(patch("/api/payment/{paymentId}/status", 1)
                        .param("status", "COMPLETED"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("\"" + ErrorMessage.PAYMENT_NOT_FOUND + "\""));
    }

    // Метод для установки аутентификации
    private void setAuthentication(String email, String role) {
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User(email, "", Collections.singleton(() -> "ROLE_" + role));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
