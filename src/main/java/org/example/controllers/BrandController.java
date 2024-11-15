package org.example.controllers;

import org.example.dto.BrandDto;
import org.example.services.interfaces.BrandService;
import org.example.annotations.BrandAnnotations.GetAllBrands;
import org.example.annotations.BrandAnnotations.GetBrandById;
import org.example.annotations.BrandAnnotations.CreateBrand;
import org.example.annotations.BrandAnnotations.UpdateBrand;
import org.example.annotations.BrandAnnotations.DeleteBrand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    // Конструкторная инъекция
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Получить все бренды
    @GetAllBrands
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    // Получить бренд по ID
    @GetBrandById
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Long id) {
        Optional<BrandDto> brandOpt = brandService.getBrandById(id);
        return brandOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новый бренд
    @CreateBrand
    public ResponseEntity<BrandDto> createBrand(@RequestBody BrandDto brandDto) {
        BrandDto created = brandService.createBrand(brandDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить бренд
    @UpdateBrand
    public ResponseEntity<BrandDto> updateBrand(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        Optional<BrandDto> updatedOpt = brandService.updateBrand(id, brandDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить бренд
    @DeleteBrand
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
