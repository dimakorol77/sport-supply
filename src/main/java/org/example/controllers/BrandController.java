package org.example.controllers;

import org.example.dto.BrandDto;
import org.example.services.interfaces.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @GetMapping
    @Operation(summary = "Получение всех брендов",
            description = "Возвращает список всех брендов",
            tags = "Бренды",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Бренды найдены")
            }
    )
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    // Получить бренд по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получение бренда по ID",
            description = "Возвращает бренд с указанным ID",
            tags = "Бренды",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Бренд найден"),
                    @ApiResponse(responseCode = "404", description = "Бренд не найден")
            }
    )
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Long id) {
        Optional<BrandDto> brandOpt = brandService.getBrandById(id);
        return brandOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новый бренд
    @PostMapping
    @Operation(summary = "Создание нового бренда",
            description = "Создает новый бренд",
            tags = "Бренды",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Бренд успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<BrandDto> createBrand(@RequestBody BrandDto brandDto) {
        BrandDto created = brandService.createBrand(brandDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить бренд
    @PutMapping("/{id}")
    @Operation(summary = "Обновление бренда",
            description = "Обновляет данные бренда по указанному ID",
            tags = "Бренды",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Бренд успешно обновлен"),
                    @ApiResponse(responseCode = "404", description = "Бренд не найден"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<BrandDto> updateBrand(@PathVariable Long id, @RequestBody BrandDto brandDto) {
        Optional<BrandDto> updatedOpt = brandService.updateBrand(id, brandDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить бренд
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление бренда",
            description = "Удаляет бренд по указанному ID",
            tags = "Бренды",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Бренд успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Бренд не найден")
            }
    )
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
