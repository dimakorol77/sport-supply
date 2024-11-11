package org.example.controllers;

import org.example.dto.PromotionDto;
import org.example.services.interfaces.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @GetMapping
    @Operation(summary = "Получение всех акций",
            description = "Возвращает список всех акций",
            tags = "Акции",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Акции найдены")
            }
    )
    public ResponseEntity<List<PromotionDto>> getAllPromotions() {
        List<PromotionDto> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    // Получить акцию по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получение акции по ID",
            description = "Возвращает акцию с указанным ID",
            tags = "Акции",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Акция найдена"),
                    @ApiResponse(responseCode = "404", description = "Акция не найдена")
            }
    )
    public ResponseEntity<PromotionDto> getPromotionById(@PathVariable Long id) {
        Optional<PromotionDto> promotionOpt = promotionService.getPromotionById(id);
        return promotionOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новую акцию
    @PostMapping
    @Operation(summary = "Создание новой акции",
            description = "Создает новую акцию",
            tags = "Акции",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Акция успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<PromotionDto> createPromotion(@Valid @RequestBody PromotionDto promotionDto) {
        PromotionDto created = promotionService.createPromotion(promotionDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить акцию
    @PutMapping("/{id}")
    @Operation(summary = "Обновление акции",
            description = "Обновляет акцию по указанному ID",
            tags = "Акции",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Акция успешно обновлена"),
                    @ApiResponse(responseCode = "404", description = "Акция не найдена"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<PromotionDto> updatePromotion(@PathVariable Long id, @Valid @RequestBody PromotionDto promotionDto) {
        Optional<PromotionDto> updatedOpt = promotionService.updatePromotion(id, promotionDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить акцию
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление акции",
            description = "Удаляет акцию по указанному ID",
            tags = "Акции",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Акция успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Акция не найдена")
            }
    )
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }

    // Получить активные акции
    @GetMapping("/active")
    @Operation(summary = "Получение активных акций",
            description = "Возвращает список активных акций",
            tags = "Акции",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Акции найдены")
            }
    )
    public ResponseEntity<List<PromotionDto>> getActivePromotions() {
        List<PromotionDto> promotions = promotionService.getActivePromotions();
        return ResponseEntity.ok(promotions);
    }
}
