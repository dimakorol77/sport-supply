package org.example.mappers;

import org.example.dto.ImageDto;
import org.example.models.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {

    public ImageDto toDto(Image image) {
        if (image == null) {
            return null;
        }
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setAltText(image.getAltText());
        dto.setProductId(image.getProduct() != null ? image.getProduct().getId() : null);
        dto.setCreatedAt(image.getCreatedAt());
        dto.setUpdatedAt(image.getUpdatedAt());
        return dto;
    }

    public Image toEntity(ImageDto dto) {
        if (dto == null) {
            return null;
        }
        Image image = new Image();
        image.setUrl(dto.getUrl());
        image.setAltText(dto.getAltText());
        // Установка продукта производится в сервисе
        image.setCreatedAt(dto.getCreatedAt());
        image.setUpdatedAt(dto.getUpdatedAt());
        return image;
    }
}
