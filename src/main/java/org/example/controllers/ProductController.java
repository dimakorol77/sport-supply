package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.DiscountDto;
import org.example.dto.ProductDto;
import org.example.dto.PromotionDto;
import org.example.services.interfaces.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Конструкторная инъекция
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Получить все продукты
    @GetMapping
    @Operation(summary = "Получение всех продуктов",
            description = "Возвращает список всех продуктов",
            tags = "Продукты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукты найдены")
            }
    )
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Получить продукт по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получение продукта по ID",
            description = "Возвращает продукт с указанным ID",
            tags = "Продукты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукт найден"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден")
            }
    )
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> productOpt = productService.getProductById(id);
        return productOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новый продукт
    @PostMapping
    @Operation(summary = "Создание нового продукта",
            description = "Создает новый продукт",
            tags = "Продукты",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Продукт успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto created = productService.createProduct(productDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить продукт
    @PutMapping("/{id}")
    @Operation(summary = "Обновление продукта",
            description = "Обновляет продукт по указанному ID",
            tags = "Продукты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Продукт успешно обновлен"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            })
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Optional<ProductDto> updatedOpt = productService.updateProduct(id, productDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить продукт
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление продукта",
            description = "Удаляет продукт по указанному ID",
            tags = "Продукты",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Продукт успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден")
            })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // Возвращаем статус 204 No Content
    }

    // Получить активные скидки для продукта
    @GetMapping("/{id}/discounts")
    @Operation(summary = "Получение активных скидок для продукта",
            description = "Возвращает список активных скидок для указанного продукта",
            tags = "Продукты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Скидки найдены"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден")
            }
    )
    public ResponseEntity<List<DiscountDto>> getActiveDiscounts(@PathVariable Long id) {
        List<DiscountDto> discounts = productService.getActiveDiscounts(id);
        return ResponseEntity.ok(discounts);
    }

    // Получить акции для продукта
    @GetMapping("/{id}/promotions")
    @Operation(summary = "Получение акций для продукта",
            description = "Возвращает список акций для указанного продукта",
            tags = "Продукты",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Акции найдены"),
                    @ApiResponse(responseCode = "404", description = "Продукт не найден")
            })
    public ResponseEntity<List<PromotionDto>> getPromotionsForProduct(@PathVariable Long id) {
        List<PromotionDto> promotions = productService.getPromotionsForProduct(id);
        return ResponseEntity.ok(promotions);
    }
}
