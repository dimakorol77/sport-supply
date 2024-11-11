package org.example.services.impl;

import org.example.dto.PromotionDto;
import org.example.mappers.PromotionMapper;
import org.example.models.Promotion;
import org.example.repositories.PromotionRepository;
import org.example.services.interfaces.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Autowired
    public PromotionServiceImpl(PromotionRepository promotionRepository,
                                PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    public List<PromotionDto> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PromotionDto> getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .map(promotionMapper::toDto);
    }

    @Override
    public PromotionDto createPromotion(PromotionDto promotionDto) {
        Promotion promotion = promotionMapper.toEntity(promotionDto);
        promotion.setCreatedAt(LocalDateTime.now());
        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(savedPromotion);
    }

    @Override
    public Optional<PromotionDto> updatePromotion(Long id, PromotionDto promotionDto) {
        return promotionRepository.findById(id).map(promotion -> {
            promotionMapper.updateEntityFromDto(promotionDto, promotion);
            promotion.setUpdatedAt(LocalDateTime.now());
            Promotion updatedPromotion = promotionRepository.save(promotion);
            return promotionMapper.toDto(updatedPromotion);
        });
    }

    @Override
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    @Override
    public List<PromotionDto> getActivePromotions() {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findByStartDateBeforeAndEndDateAfter(now, now).stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }
}
