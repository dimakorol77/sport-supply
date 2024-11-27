package org.example.controllers;

import org.example.annotations.ProductAnnotations.*;
import org.example.dto.DiscountDto;
import org.example.dto.ProductDto;
import org.example.dto.PromotionDto;
import org.example.services.interfaces.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetAllProducts
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetProductById
    @PreAuthorize("permitAll()")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }


    @CreateProduct
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto created = productService.createProduct(productDto);
        return ResponseEntity.status(201).body(created);
    }


    @UpdateProduct
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        ProductDto updated = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updated);
    }


    @DeleteProduct
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetActiveDiscounts
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<DiscountDto>> getActiveDiscounts(@PathVariable Long id) {
        List<DiscountDto> discounts = productService.getActiveDiscounts(id);
        return ResponseEntity.ok(discounts);
    }


    @GetPromotionsForProduct
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PromotionDto>> getPromotionsForProduct(@PathVariable Long id) {
        List<PromotionDto> promotions = productService.getPromotionsForProduct(id);
        return ResponseEntity.ok(promotions);
    }


    @FilterAndSortProducts
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDto>> filterAndSortProducts(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean hasDiscount,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "true") Boolean asc) {

        List<ProductDto> products = productService.filterAndSortProducts(minPrice, maxPrice, categoryId, hasDiscount, sortBy, asc);
        return ResponseEntity.ok(products);
    }
}
