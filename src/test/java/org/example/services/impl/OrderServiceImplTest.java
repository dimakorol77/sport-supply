// src/test/java/org/example/services/impl/OrderServiceImplTest.java

package org.example.services.impl;

import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.enums.DeliveryMethod;
import org.example.enums.OrderStatus;
import org.example.enums.Role;
import org.example.exceptions.OrderCancellationException;
import org.example.exceptions.OrderNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.OrderMapper;
import org.example.models.*;
import org.example.repositories.OrderRepository;
import org.example.repositories.OrderStatusHistoryRepository;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDto orderDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setOrderItems(new ArrayList<>());

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(1L);
        orderDto.setStatus(OrderStatus.CREATED);
    }

    @Test
    void testGetOrdersByUserId() {
        when(orderRepository.findByUserId(1L)).thenReturn(Arrays.asList(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> orders = orderService.getOrdersByUserId(1L);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(orderDto, orders.get(0));
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        List<OrderDto> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(orderDto, orders.get(0));
    }

    @Test
    void testUpdateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.updateOrderStatus(1L, OrderStatus.PAID);

        assertNotNull(result);
        assertEquals(orderDto, result);
        assertEquals(OrderStatus.PAID, order.getStatus());
        verify(orderStatusHistoryRepository, times(1)).save(any(OrderStatusHistory.class));
    }

    @Test
    void testUpdateOrderStatus_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.PAID));
        assertEquals(ErrorMessage.ORDER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCancelOrderAndCheckOwnership_Success() {
        order.setStatus(OrderStatus.CREATED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        assertDoesNotThrow(() -> orderService.cancelOrderAndCheckOwnership(1L, 1L));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderStatusHistoryRepository, times(1)).save(any(OrderStatusHistory.class));
    }

    @Test
    void testCancelOrderAndCheckOwnership_NotCancelable() {
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderCancellationException exception = assertThrows(OrderCancellationException.class, () -> orderService.cancelOrderAndCheckOwnership(1L, 1L));
        assertEquals(ErrorMessage.ORDER_CANNOT_BE_CANCELLED, exception.getMessage());
    }

    @Test
    void testCancelOrderAndCheckOwnership_NotOwner() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> orderService.cancelOrderAndCheckOwnership(1L, 2L));
        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());
    }

    @Test
    void testIsOrderOwner_True() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        boolean result = orderService.isOrderOwner(1L, 1L);

        assertTrue(result);
    }

    @Test
    void testIsOrderOwner_False() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        boolean result = orderService.isOrderOwner(1L, 2L);

        assertFalse(result);
    }

    @Test
    void testGetOrderByIdAndCheckOwnership_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.getOrderByIdAndCheckOwnership(1L, 1L);

        assertNotNull(result);
        assertEquals(orderDto, result);
    }

    @Test
    void testGetOrderByIdAndCheckOwnership_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderService.getOrderByIdAndCheckOwnership(1L, 1L));
        assertEquals(ErrorMessage.ORDER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetOrderByIdAndCheckOwnership_AccessDenied() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> orderService.getOrderByIdAndCheckOwnership(1L, 2L));
        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());
    }
}
