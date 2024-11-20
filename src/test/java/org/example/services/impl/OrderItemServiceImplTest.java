// src/test/java/org/example/services/impl/OrderItemServiceImplTest.java

package org.example.services.impl;

import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;
import org.example.exceptions.OrderItemNotFoundException;
import org.example.exceptions.OrderNotFoundException;
import org.example.exceptions.ProductNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.OrderItemMapper;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.example.repositories.OrderItemRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private Order order;
    private Product product;
    private OrderItem orderItem;
    private OrderItemCreateDto orderItemCreateDto;
    private OrderItemDto orderItemDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new Order();
        order.setId(1L);
        order.setStatus(null); // Предположим, статус не важен для этого теста

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
        orderItem.setPrice(new BigDecimal("50.00"));
        orderItem.setQuantity(2);

        orderItemCreateDto = new OrderItemCreateDto();
        orderItemCreateDto.setProductId(1L);
        orderItemCreateDto.setPrice(new BigDecimal("50.00"));
        orderItemCreateDto.setQuantity(2);

        orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setProductName("Test Product");
        orderItemDto.setProductDescription("Test Description");
        orderItemDto.setPrice(new BigDecimal("50.00"));
        orderItemDto.setQuantity(2);
    }

    @Test
    void testCreateOrderItem_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemMapper.toEntity(orderItemCreateDto, product)).thenReturn(orderItem);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        OrderItemDto result = orderItemService.createOrderItem(orderItemCreateDto, 1L);

        assertNotNull(result);
        assertEquals(orderItemDto, result);
        verify(orderItemRepository, times(1)).save(orderItem);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCreateOrderItem_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> orderItemService.createOrderItem(orderItemCreateDto, 1L));
        assertEquals(ErrorMessage.ORDER_NOT_FOUND, exception.getMessage());
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    void testCreateOrderItem_ProductNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> orderItemService.createOrderItem(orderItemCreateDto, 1L));
        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    void testGetOrderItemsByOrderId() {
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Arrays.asList(orderItem));
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        List<OrderItemDto> orderItems = orderItemService.getOrderItemsByOrderId(1L);

        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        assertEquals(orderItemDto, orderItems.get(0));
    }

    @Test
    void testUpdateOrderItem_Success() {
        OrderItemCreateDto updateDto = new OrderItemCreateDto();
        updateDto.setProductId(1L);
        updateDto.setPrice(new BigDecimal("60.00"));
        updateDto.setQuantity(3);

        OrderItem updatedOrderItem = new OrderItem();
        updatedOrderItem.setId(1L);
        updatedOrderItem.setOrder(order);
        updatedOrderItem.setProductId(1L);
        updatedOrderItem.setProductName("Test Product");
        updatedOrderItem.setProductDescription("Test Description");
        updatedOrderItem.setPrice(new BigDecimal("60.00"));
        updatedOrderItem.setQuantity(3);

        OrderItemDto updatedOrderItemDto = new OrderItemDto();
        updatedOrderItemDto.setProductId(1L);
        updatedOrderItemDto.setProductName("Test Product");
        updatedOrderItemDto.setProductDescription("Test Description");
        updatedOrderItemDto.setPrice(new BigDecimal("60.00"));
        updatedOrderItemDto.setQuantity(3);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(orderItemMapper).updateEntityFromCreateDto(updateDto, orderItem, product);
        when(orderItemRepository.save(orderItem)).thenReturn(updatedOrderItem);
        when(orderItemMapper.toDto(updatedOrderItem)).thenReturn(updatedOrderItemDto);
        when(orderRepository.save(order)).thenReturn(order);

        OrderItemDto result = orderItemService.updateOrderItem(1L, updateDto);

        assertNotNull(result);
        assertEquals(updatedOrderItemDto, result);
        verify(orderItemRepository, times(1)).save(orderItem);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateOrderItem_OrderItemNotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class, () -> orderItemService.updateOrderItem(1L, orderItemCreateDto));
        assertEquals(ErrorMessage.ORDER_ITEM_NOT_FOUND, exception.getMessage());
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    void testDeleteOrderItem_Success() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(order.getOrderItems()).thenReturn(new ArrayList<>(Arrays.asList(orderItem)));
        when(orderRepository.save(order)).thenReturn(order);

        assertDoesNotThrow(() -> orderItemService.deleteOrderItem(1L));
        verify(orderItemRepository, times(1)).delete(orderItem);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrderItem_OrderItemNotFound() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class, () -> orderItemService.deleteOrderItem(1L));
        assertEquals(ErrorMessage.ORDER_ITEM_NOT_FOUND, exception.getMessage());
        verify(orderItemRepository, never()).delete(any());
    }
}
