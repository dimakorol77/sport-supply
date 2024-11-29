package org.example.mappers;

import org.example.dto.PromotionDto;
import org.example.models.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionDto toDto(Promotion promotion) {
        if (promotion == null) {
            return null;
        }
        PromotionDto dto = new PromotionDto();
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        return dto;
    }

    public Promotion toEntity(PromotionDto dto) {
        if (dto == null) {
            return null;
        }
        Promotion promotion = new Promotion();
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        return promotion;
    }


    public void updateEntityFromUpdateDto(PromotionDto dto, Promotion promotion) {
        if (dto == null || promotion == null) {
            return;
        }
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
    }}
