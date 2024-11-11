package org.example.services.interfaces;

import org.example.dto.PromotionDto;
import org.example.models.Promotion;

import java.util.List;
import java.util.Optional;

public interface PromotionService {
    List<PromotionDto> getAllPromotions();
    Optional<PromotionDto> getPromotionById(Long id);
    PromotionDto createPromotion(PromotionDto promotionDto);
    Optional<PromotionDto> updatePromotion(Long id, PromotionDto promotionDto);
    void deletePromotion(Long id);
    List<PromotionDto> getActivePromotions();
}

