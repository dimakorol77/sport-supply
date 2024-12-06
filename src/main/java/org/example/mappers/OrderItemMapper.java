package org.example.mappers;

import org.example.dto.OrderItemCreateDto;
import org.example.dto.OrderItemDto;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDto toDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem);
    }


    public OrderItem toEntity(OrderItemCreateDto orderItemCreateDto, Product product) {
        if (orderItemCreateDto == null || product == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
        if (product.getCategory() != null) {
            orderItem.setProductCategoryName(product.getCategory().getName());
        } else {
            orderItem.setProductCategoryName("Uncategorized");
        }
        orderItem.setPrice(orderItemCreateDto.getPrice());
        orderItem.setQuantity(orderItemCreateDto.getQuantity());
        return orderItem;
    }

    public void updateEntityFromCreateDto(OrderItemCreateDto orderItemCreateDto, OrderItem orderItem, Product product) {
        if (orderItemCreateDto == null || orderItem == null || product == null) {
            return;
        }
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductDescription(product.getDescription());
        if (product.getCategory() != null) {
            orderItem.setProductCategoryName(product.getCategory().getName());
        } else {
            orderItem.setProductCategoryName("Uncategorized");
        }
        orderItem.setPrice(orderItemCreateDto.getPrice());
        orderItem.setQuantity(orderItemCreateDto.getQuantity());
    }

}
