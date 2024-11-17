package org.example.services.interfaces;

import org.example.dto.PromotionDto;
import java.util.List;

public interface PromotionService {
    List<PromotionDto> getAllPromotions();
    PromotionDto getPromotionById(Long id);
    PromotionDto createPromotion(PromotionDto promotionDto);
    PromotionDto updatePromotion(Long id, PromotionDto promotionDto);
    void deletePromotion(Long id);
    void addProductToPromotion(Long promotionId, Long productId);
    void removeProductFromPromotion(Long promotionId, Long productId);
    List<PromotionDto> getPromotionsForProduct(Long productId);
}
