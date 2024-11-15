package org.example.mappers;

import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    // Преобразование сущности OrderItem в DTO OrderItemDto
    public OrderItemDto toDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem);
    }

    // Преобразование DTO OrderItemDto и Product в сущность OrderItem
    public OrderItem toEntity(OrderItemDto orderItemDto, Product product) {
        if (orderItemDto == null || product == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
     //   orderItem.setProductImageUrl(getProductImageUrl(product));
        orderItem.setProductCategoryName(product.getCategory().getName());
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
        return orderItem;
    }

    // Обновление сущности OrderItem на основе данных из DTO OrderItemDto и Product
    public void updateEntityFromDto(OrderItemDto orderItemDto, OrderItem orderItem, Product product) {
        if (orderItemDto == null || orderItem == null || product == null) {
            return;
        }
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
     //   orderItem.setProductImageUrl(getProductImageUrl(product));
        orderItem.setProductCategoryName(product.getCategory().getName());
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
    }

    // Метод для получения URL изображения продукта
    private String getProductImageUrl(Product product) {
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            return product.getImages().get(0).getUrl();
        }
        return null;
    }
}
