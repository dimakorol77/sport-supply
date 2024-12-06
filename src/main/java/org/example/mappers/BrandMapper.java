package org.example.mappers;

import org.example.dto.BrandDto;
import org.example.models.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandDto toDto(Brand brand) {
        if (brand == null) {
            return null;
        }
        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setDescription(brand.getDescription());
        return dto;
    }

    public Brand toEntity(BrandDto dto) {
        if (dto == null) {
            return null;
        }
        Brand brand = new Brand();
        brand.setName(dto.getName());
        brand.setDescription(dto.getDescription());
        return brand;
    }

    public void updateEntityFromDto(BrandDto dto, Brand brand) {
        if (dto == null || brand == null) {
            return;
        }
        brand.setName(dto.getName());
        brand.setDescription(dto.getDescription());
    }
}
