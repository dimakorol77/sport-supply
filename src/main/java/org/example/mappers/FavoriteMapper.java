package org.example.mappers;

import org.example.dto.ProductDto;
import org.example.models.Product;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {
    // Преобразование сущности Product в ProductDto
    public ProductDto toProductDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        // Добавьте другие необходимые поля
        return dto;
    }
}
