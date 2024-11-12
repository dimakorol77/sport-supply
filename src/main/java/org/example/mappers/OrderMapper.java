package org.example.mappers;

import org.example.dto.OrderCreateDto;
import org.example.dto.OrderDto;
import org.example.dto.OrderItemDto;
import org.example.enums.OrderStatus;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.example.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    // Преобразуем Order в OrderDto
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());  // Устанавливаем ID заказа
        dto.setTotalAmount(order.getTotalAmount());  // Общая сумма заказа
        dto.setDeliveryMethod(order.getDeliveryMethod());  // Метод доставки
        dto.setDeliveryAddress(order.getDeliveryAddress());  // Адрес доставки
        dto.setContactInfo(order.getContactInfo());  // Контактная информация
        dto.setUserId(order.getUser().getId());  // Получаем ID пользователя
        dto.setOrderItems(order.getOrderItems().stream()
                .map(item -> new OrderItemDto(item.getProduct().getId(), item.getProduct().getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList()));  // Преобразуем товары в заказе в OrderItemDto
        dto.setStatus(order.getStatus());  // Статус заказа
        dto.setCreatedAt(order.getCreatedAt());  // Дата создания заказа
        dto.setUpdatedAt(order.getUpdatedAt());  // Дата обновления заказа
        return dto;
    }

    // Преобразуем OrderCreateDto в Order (при создании заказа)
    public Order toEntity(OrderCreateDto orderCreateDto, User user) {
        if (orderCreateDto == null) {
            return null;
        }

        Order order = new Order();
        order.setUser(user);  // Связываем заказ с пользователем
        order.setTotalAmount(orderCreateDto.getTotalAmount());  // Общая сумма заказа
        order.setDeliveryMethod(orderCreateDto.getDeliveryMethod());  // Метод доставки
        order.setDeliveryAddress(orderCreateDto.getDeliveryAddress());  // Адрес доставки
        order.setContactInfo(orderCreateDto.getContactInfo());  // Контактная информация
        order.setOrderItems(orderCreateDto.getOrderItems().stream()
                .map(itemDto -> {
                    Product product = new Product();  // Пример создания продукта, если необходимо
                    product.setId(itemDto.getProductId());
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);  // Связываем товар с заказом
                    orderItem.setPrice(itemDto.getPrice());
                    orderItem.setQuantity(itemDto.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toList()));  // Преобразуем список OrderItemDto в сущности OrderItem
        order.setStatus(OrderStatus.CREATED);  // Статус по умолчанию
        order.setCreatedAt(LocalDateTime.now());  // Устанавливаем дату создания
        order.setUpdatedAt(LocalDateTime.now());  // Устанавливаем дату обновления
        return order;
    }

    // Обновление сущности Order на основе OrderDto
    public void updateEntityFromDto(OrderDto orderDto, Order order) {
        if (orderDto == null || order == null) {
            return;
        }

        order.setTotalAmount(orderDto.getTotalAmount());  // Обновляем общую сумму заказа
        order.setDeliveryMethod(orderDto.getDeliveryMethod());  // Обновляем метод доставки
        order.setDeliveryAddress(orderDto.getDeliveryAddress());  // Обновляем адрес доставки
        order.setContactInfo(orderDto.getContactInfo());  // Обновляем контактную информацию
        order.setStatus(orderDto.getStatus());  // Обновляем статус заказа
        order.setUpdatedAt(LocalDateTime.now());  // Обновляем дату обновления
    }
}
