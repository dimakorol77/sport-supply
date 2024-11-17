package org.example.controllers;

import org.example.dto.PromotionDto;
import org.example.services.interfaces.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // Получить все акции
    @GetMapping
    public ResponseEntity<List<PromotionDto>> getAllPromotions() {
        List<PromotionDto> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    // Получить акцию по ID
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDto> getPromotionById(@PathVariable Long id) {
        PromotionDto promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    }

    // Создать новую акцию
    @PostMapping
    public ResponseEntity<PromotionDto> createPromotion(@RequestBody PromotionDto promotionDto) {
        PromotionDto createdPromotion = promotionService.createPromotion(promotionDto);
        return ResponseEntity.status(201).body(createdPromotion);
    }

    // Обновить акцию
    @PutMapping("/{id}")
    public ResponseEntity<PromotionDto> updatePromotion(@PathVariable Long id, @RequestBody PromotionDto promotionDto) {
        PromotionDto updatedPromotion = promotionService.updatePromotion(id, promotionDto);
        return ResponseEntity.ok(updatedPromotion);
    }

    // Удалить акцию
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    // Добавить продукт в акцию
    @PostMapping("/{promotionId}/products/{productId}")
    public ResponseEntity<Void> addProductToPromotion(@PathVariable Long promotionId, @PathVariable Long productId) {
        promotionService.addProductToPromotion(promotionId, productId);
        return ResponseEntity.status(201).build();
    }

    // Удалить продукт из акции
    @DeleteMapping("/{promotionId}/products/{productId}")
    public ResponseEntity<Void> removeProductFromPromotion(@PathVariable Long promotionId, @PathVariable Long productId) {
        promotionService.removeProductFromPromotion(promotionId, productId);
        return ResponseEntity.noContent().build();
    }

    // Получить акции для продукта
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PromotionDto>> getPromotionsForProduct(@PathVariable Long productId) {
        List<PromotionDto> promotions = promotionService.getPromotionsForProduct(productId);
        return ResponseEntity.ok(promotions);
    }
}
