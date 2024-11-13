package org.example.controllers;

import org.example.dto.DiscountDto;
import org.example.services.interfaces.DiscountService;
import org.example.annotations.DiscountAnnotations.GetAllDiscounts;
import org.example.annotations.DiscountAnnotations.GetDiscountById;
import org.example.annotations.DiscountAnnotations.CreateDiscount;
import org.example.annotations.DiscountAnnotations.UpdateDiscount;
import org.example.annotations.DiscountAnnotations.DeleteDiscount;
import org.example.annotations.DiscountAnnotations.GetActiveDiscountsForProduct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
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
    @GetAllDiscounts
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        List<DiscountDto> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }

    // Получить скидку по ID
    @GetDiscountById
    public ResponseEntity<DiscountDto> getDiscountById(@PathVariable Long id) {
        Optional<DiscountDto> discountOpt = discountService.getDiscountById(id);
        return discountOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новую скидку
    @CreateDiscount
    public ResponseEntity<DiscountDto> createDiscount(@Valid @RequestBody DiscountDto discountDto) {
        DiscountDto created = discountService.createDiscount(discountDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить скидку
    @UpdateDiscount
    public ResponseEntity<DiscountDto> updateDiscount(@PathVariable Long id, @Valid @RequestBody DiscountDto discountDto) {
        Optional<DiscountDto> updatedOpt = discountService.updateDiscount(id, discountDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить скидку
    @DeleteDiscount
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    // Получить активные скидки для продукта
    @GetActiveDiscountsForProduct
    public ResponseEntity<List<DiscountDto>> getActiveDiscountsForProduct(@PathVariable Long productId) {
        List<DiscountDto> discounts = discountService.getActiveDiscountsForProduct(productId);
        return ResponseEntity.ok(discounts);
    }
}
