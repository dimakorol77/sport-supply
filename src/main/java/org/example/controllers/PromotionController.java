package org.example.controllers;


import org.example.annotations.PromotionAnnotations.*;
import org.example.dto.PromotionDto;
import org.example.services.interfaces.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

    @GetAllPromotions
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PromotionDto>> getAllPromotions() {
        List<PromotionDto> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    @GetPromotionById
    @PreAuthorize("permitAll()")
    public ResponseEntity<PromotionDto> getPromotionById(@PathVariable Long id) {
        PromotionDto promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(promotion);
    }

    @CreatePromotion
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionDto> createPromotion(
            @RequestBody @Validated(PromotionDto.OnCreate.class) PromotionDto promotionDto) {
        PromotionDto createdPromotion = promotionService.createPromotion(promotionDto);
        return ResponseEntity.status(201).body(createdPromotion);
    }

    @UpdatePromotion
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionDto> updatePromotion(
            @PathVariable Long id,
            @Validated(PromotionDto.OnUpdate.class) @RequestBody PromotionDto promotionDto) {
        promotionDto.setId(id);
        PromotionDto updatedPromotion = promotionService.updatePromotion(id, promotionDto);
        return ResponseEntity.ok(updatedPromotion);
    }

    @DeletePromotion
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    @AddProductToPromotion
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addProductToPromotion(@PathVariable Long promotionId, @PathVariable Long productId) {
        promotionService.addProductToPromotion(promotionId, productId);
        return ResponseEntity.status(201).build();
    }

    @RemoveProductFromPromotion
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeProductFromPromotion(@PathVariable Long promotionId, @PathVariable Long productId) {
        promotionService.removeProductFromPromotion(promotionId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetPromotionsForProduct
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PromotionDto>> getPromotionsForProduct(@PathVariable Long productId) {
        List<PromotionDto> promotions = promotionService.getPromotionsForProduct(productId);
        return ResponseEntity.ok(promotions);
    }
}

