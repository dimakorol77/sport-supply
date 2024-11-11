package org.example.controllers;

import org.example.dto.DiscountDto;
import org.example.services.interfaces.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    // Конструкторная инъекция
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    // Получить все скидки
    @GetMapping
    @Operation(summary = "Получение всех скидок",
            description = "Возвращает список всех скидок",
            tags = "Скидки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Скидки найдены")
            }
    )
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        List<DiscountDto> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }

    // Получить скидку по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получение скидки по ID",
            description = "Возвращает скидку с указанным ID",
            tags = "Скидки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Скидка найдена"),
                    @ApiResponse(responseCode = "404", description = "Скидка не найдена")
            }
    )
    public ResponseEntity<DiscountDto> getDiscountById(@PathVariable Long id) {
        Optional<DiscountDto> discountOpt = discountService.getDiscountById(id);
        return discountOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новую скидку
    @PostMapping
    @Operation(summary = "Создание новой скидки",
            description = "Создает новую скидку",
            tags = "Скидки",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Скидка успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<DiscountDto> createDiscount(@Valid @RequestBody DiscountDto discountDto) {
        DiscountDto created = discountService.createDiscount(discountDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить скидку
    @PutMapping("/{id}")
    @Operation(summary = "Обновление скидки",
            description = "Обновляет скидку по указанному ID",
            tags = "Скидки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Скидка успешно обновлена"),
                    @ApiResponse(responseCode = "404", description = "Скидка не найдена"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<DiscountDto> updateDiscount(@PathVariable Long id, @Valid @RequestBody DiscountDto discountDto) {
        Optional<DiscountDto> updatedOpt = discountService.updateDiscount(id, discountDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить скидку
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление скидки",
            description = "Удаляет скидку по указанному ID",
            tags = "Скидки",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Скидка успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Скидка не найдена")
            }
    )
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    // Получить активные скидки для продукта
    @GetMapping("/product/{productId}/active")
    @Operation(summary = "Получение активных скидок для продукта",
            description = "Возвращает список активных скидок для указанного продукта",
            tags = "Скидки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Скидки найдены"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден")
            }
    )
    public ResponseEntity<List<DiscountDto>> getActiveDiscountsForProduct(@PathVariable Long productId) {
        List<DiscountDto> discounts = discountService.getActiveDiscountsForProduct(productId);
        return ResponseEntity.ok(discounts);
    }
}
