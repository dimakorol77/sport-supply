package org.example.controllers;

import org.example.models.Discount;
import org.example.models.Product;
import org.example.models.Promotion;
import org.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Получить все продукты
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Получить продукт по ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.getProductById(id);
        return productOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новый продукт
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseEntity.ok(created);
    }

    // Обновить продукт
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<Product> updatedOpt = productService.updateProduct(id, product);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить продукт
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // Получить активные скидки для продукта
    @GetMapping("/{id}/discounts")
    public ResponseEntity<List<Discount>> getActiveDiscounts(@PathVariable Long id) {
        List<Discount> discounts = productService.getActiveDiscounts(id);
        return ResponseEntity.ok(discounts);
    }

    // Получить акции для продукта
    @GetMapping("/{id}/promotions")
    public ResponseEntity<List<Promotion>> getPromotionsForProduct(@PathVariable Long id) {
        List<Promotion> promotions = productService.getPromotionsForProduct(id);
        return ResponseEntity.ok(promotions);
    }
}
