package org.example.controllers;

import org.example.dto.PromotionDto;
import org.example.services.interfaces.PromotionService;
import org.example.annotations.PromotionAnnotations.GetAllPromotions;
import org.example.annotations.PromotionAnnotations.GetPromotionById;
import org.example.annotations.PromotionAnnotations.CreatePromotion;
import org.example.annotations.PromotionAnnotations.UpdatePromotion;
import org.example.annotations.PromotionAnnotations.DeletePromotion;
import org.example.annotations.PromotionAnnotations.GetActivePromotions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    // Конструкторная инъекция
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // Получить все акции
    @GetAllPromotions
    public ResponseEntity<List<PromotionDto>> getAllPromotions() {
        List<PromotionDto> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    // Получить акцию по ID
    @GetPromotionById
    public ResponseEntity<PromotionDto> getPromotionById(@PathVariable Long id) {
        Optional<PromotionDto> promotionOpt = promotionService.getPromotionById(id);
        return promotionOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новую акцию
    @CreatePromotion
    public ResponseEntity<PromotionDto> createPromotion(@Valid @RequestBody PromotionDto promotionDto) {
        PromotionDto created = promotionService.createPromotion(promotionDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить акцию
    @UpdatePromotion
    public ResponseEntity<PromotionDto> updatePromotion(@PathVariable Long id, @Valid @RequestBody PromotionDto promotionDto) {
        Optional<PromotionDto> updatedOpt = promotionService.updatePromotion(id, promotionDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить акцию
    @DeletePromotion
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    // Получить активные акции
    @GetActivePromotions
    public ResponseEntity<List<PromotionDto>> getActivePromotions() {
        List<PromotionDto> promotions = promotionService.getActivePromotions();
        return ResponseEntity.ok(promotions);
    }
}
