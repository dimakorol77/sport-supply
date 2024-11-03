package org.example.services.interfaces;

import org.example.models.Promotion;

import java.util.List;
import java.util.Optional;

public interface PromotionService {
    List<Promotion> getAllPromotions();
    Optional<Promotion> getPromotionById(Long id);
    Promotion createPromotion(Promotion promotion);
    Optional<Promotion> updatePromotion(Long id, Promotion updatedPromotion);
    void deletePromotion(Long id);
    List<Promotion> getActivePromotions();
}
