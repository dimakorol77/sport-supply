package org.example.services.impl;

import org.example.models.Promotion;
import org.example.repositories.PromotionRepository;
import org.example.services.interfaces.PromotionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    // Используем конструкторную инъекцию
    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    // Получить все акции
    @Override
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    // Получить акцию по ID
    @Override
    public Optional<Promotion> getPromotionById(Long id) {
        return promotionRepository.findById(id);
    }

    // Создать новую акцию
    @Override
    public Promotion createPromotion(Promotion promotion) {
        promotion.setCreatedAt(LocalDateTime.now());
        return promotionRepository.save(promotion);
    }

    // Обновить акцию
    @Override
    public Optional<Promotion> updatePromotion(Long id, Promotion updatedPromotion) {
        return promotionRepository.findById(id).map(promotion -> {
            promotion.setName(updatedPromotion.getName());
            promotion.setDescription(updatedPromotion.getDescription());
            promotion.setStartDate(updatedPromotion.getStartDate());
            promotion.setEndDate(updatedPromotion.getEndDate());
            promotion.setUpdatedAt(LocalDateTime.now());
            // Обновление других полей при необходимости
            return promotionRepository.save(promotion);
        });
    }

    // Удалить акцию
    @Override
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    // Получить активные акции
    @Override
    public List<Promotion> getActivePromotions() {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByStartDateBeforeAndEndDateAfter(now, now);
    }
}
