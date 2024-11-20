// src/test/java/org/example/services/impl/PaymentServiceImplTest.java

package org.example.services.impl;

import org.example.dto.PaymentRequestDto;
import org.example.dto.PaymentResponseDto;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentStatus;
import org.example.exceptions.OrderNotFoundException;
import org.example.exceptions.PaymentAlreadyExistsException;
import org.example.exceptions.PaymentNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.PaymentMapper;
import org.example.models.Order;
import org.example.models.Payment;
import org.example.models.User;
import org.example.repositories.OrderRepository;
import org.example.repositories.PaymentRepository;
import org.example.services.interfaces.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequestDto paymentRequestDto;
    private Payment payment;
    private PaymentResponseDto paymentResponseDto;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setOrderId(1L);
        paymentRequestDto.setAmount(new BigDecimal("100.00"));

        order = new Order();
        order.setId(1L);
        User user = new User();
        user.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        payment = new Payment();
        payment.setId(1L);
        payment.setOrder(order);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(1L);
        paymentResponseDto.setOrderId(1L);
        paymentResponseDto.setAmount(new BigDecimal("100.00"));
        paymentResponseDto.setStatus(PaymentStatus.PENDING);
        paymentResponseDto.setCreatedAt(payment.getCreatedAt());
        paymentResponseDto.setUpdatedAt(payment.getUpdatedAt());
    }

    @Test
    void createPayment_Success() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(paymentMapper.toEntity(paymentRequestDto, order)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponseDto(payment)).thenReturn(paymentResponseDto);

        // Act
        PaymentResponseDto result = paymentService.createPayment(paymentRequestDto, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(paymentResponseDto.getId(), result.getId());
        assertEquals(paymentResponseDto.getOrderId(), result.getOrderId());
        assertEquals(paymentResponseDto.getAmount(), result.getAmount());
        assertEquals(paymentResponseDto.getStatus(), result.getStatus());

        verify(orderRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).findByOrderId(1L);
        verify(paymentMapper, times(1)).toEntity(paymentRequestDto, order);
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentMapper, times(1)).toResponseDto(payment);
    }

    @Test
    void createPayment_OrderNotFound() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> paymentService.createPayment(paymentRequestDto, 1L));

        assertEquals(ErrorMessage.ORDER_NOT_FOUND, exception.getMessage());

        verify(orderRepository, times(1)).findById(1L);
        verify(paymentRepository, never()).findByOrderId(anyLong());
        verify(paymentMapper, never()).toEntity(any(), any());
        verify(paymentRepository, never()).save(any());
        verify(paymentMapper, never()).toResponseDto(any());
    }

    @Test
    void createPayment_AccessDenied() {
        // Arrange
        Order otherOrder = new Order();
        otherOrder.setId(2L);
        User otherUser = new User();
        otherUser.setId(2L);
        otherOrder.setUser(otherUser);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(otherOrder));

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> paymentService.createPayment(paymentRequestDto, 1L));

        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());

        verify(orderRepository, times(1)).findById(1L);
        verify(paymentRepository, never()).findByOrderId(anyLong());
        verify(paymentMapper, never()).toEntity(any(), any());
        verify(paymentRepository, never()).save(any());
        verify(paymentMapper, never()).toResponseDto(any());
    }

    @Test
    void createPayment_PaymentAlreadyExists() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(payment));

        // Act & Assert
        PaymentAlreadyExistsException exception = assertThrows(PaymentAlreadyExistsException.class,
                () -> paymentService.createPayment(paymentRequestDto, 1L));

        assertEquals(ErrorMessage.PAYMENT_ALREADY_EXISTS, exception.getMessage());

        verify(orderRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).findByOrderId(1L);
        verify(paymentMapper, never()).toEntity(any(), any());
        verify(paymentRepository, never()).save(any());
        verify(paymentMapper, never()).toResponseDto(any());
    }

    @Test
    void getPaymentStatus_Success() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        when(paymentMapper.toResponseDto(payment)).thenReturn(paymentResponseDto);

        // Act
        PaymentResponseDto result = paymentService.getPaymentStatus(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(paymentResponseDto.getId(), result.getId());
        assertEquals(paymentResponseDto.getOrderId(), result.getOrderId());
        assertEquals(paymentResponseDto.getAmount(), result.getAmount());
        assertEquals(paymentResponseDto.getStatus(), result.getStatus());

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentMapper, times(1)).toResponseDto(payment);
    }

    @Test
    void getPaymentStatus_PaymentNotFound() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class,
                () -> paymentService.getPaymentStatus(1L, 1L));

        assertEquals(ErrorMessage.PAYMENT_NOT_FOUND, exception.getMessage());

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentMapper, never()).toResponseDto(any());
    }

    @Test
    void getPaymentStatus_AccessDenied() {
        // Arrange
        User differentUser = new User();
        differentUser.setId(2L);
        order.setUser(differentUser);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // Act & Assert
        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> paymentService.getPaymentStatus(1L, 1L));

        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentMapper, never()).toResponseDto(any());
    }

    @Test
    void updatePaymentStatus_Success() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Payment updatedPayment = new Payment();
        updatedPayment.setId(1L);
        updatedPayment.setOrder(order);
        updatedPayment.setAmount(new BigDecimal("100.00"));
        updatedPayment.setStatus(PaymentStatus.COMPLETED);
        updatedPayment.setCreatedAt(payment.getCreatedAt());
        updatedPayment.setUpdatedAt(LocalDateTime.now());

        when(paymentRepository.save(payment)).thenReturn(updatedPayment);

        // Act
        paymentService.updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        // Assert
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertNotNull(payment.getUpdatedAt());

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updatePaymentStatus_PaymentNotFound() {
        // Arrange
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class,
                () -> paymentService.updatePaymentStatus(1L, PaymentStatus.COMPLETED));

        assertEquals(ErrorMessage.PAYMENT_NOT_FOUND, exception.getMessage());

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updatePaymentStatusByOrder_Success() {
        // Arrange
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(payment));

        Payment updatedPayment = new Payment();
        updatedPayment.setId(1L);
        updatedPayment.setOrder(order);
        updatedPayment.setAmount(new BigDecimal("100.00"));
        updatedPayment.setStatus(PaymentStatus.FAILED);
        updatedPayment.setCreatedAt(payment.getCreatedAt());
        updatedPayment.setUpdatedAt(LocalDateTime.now());

        when(paymentRepository.save(payment)).thenReturn(updatedPayment);

        // Act
        paymentService.updatePaymentStatusByOrder(1L, PaymentStatus.FAILED);

        // Assert
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        assertNotNull(payment.getUpdatedAt());

        verify(paymentRepository, times(1)).findByOrderId(1L);
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updatePaymentStatusByOrder_PaymentNotFound() {
        // Act & Assert
        PaymentNotFoundException exception = assertThrows(PaymentNotFoundException.class,
                () -> paymentService.updatePaymentStatusByOrder(1L, PaymentStatus.COMPLETED));

        assertEquals(ErrorMessage.PAYMENT_NOT_FOUND, exception.getMessage());

        verify(paymentRepository, times(1)).findByOrderId(1L);
        verify(paymentRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }
}
