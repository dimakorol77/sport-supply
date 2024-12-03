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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }


    @GetAllDiscounts
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        List<DiscountDto> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }


    @GetDiscountById
    public ResponseEntity<DiscountDto> getDiscountById(@PathVariable Long id) {
        DiscountDto discount = discountService.getDiscountById(id);
        return ResponseEntity.ok(discount);
    }


    @CreateDiscount
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiscountDto> createDiscount(
            @Validated(DiscountDto.OnCreate.class) @RequestBody DiscountDto discountDto) {
        DiscountDto created = discountService.createDiscount(discountDto);
        return ResponseEntity.status(201).body(created);
    }



    @UpdateDiscount
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiscountDto> updateDiscount(
            @PathVariable Long id,
            @Validated(DiscountDto.OnUpdate.class) @RequestBody DiscountDto discountDto) {
        DiscountDto updatedDiscount = discountService.updateDiscount(id, discountDto);
        return ResponseEntity.ok(updatedDiscount);
    }



    @DeleteDiscount
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }


    @GetActiveDiscountsForProduct
    public ResponseEntity<List<DiscountDto>> getActiveDiscountsForProduct(@PathVariable Long productId) {
        List<DiscountDto> discounts = discountService.getActiveDiscountsForProduct(productId);
        return ResponseEntity.ok(discounts);
    }
}
