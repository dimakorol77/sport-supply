package org.example.services.interfaces;

import org.example.dto.DiscountDto;
import org.example.dto.ProductDto;
import org.example.dto.PromotionDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> getAllProducts();
    Optional<ProductDto> getProductById(Long id);
    ProductDto createProduct(ProductDto productDto);
    Optional<ProductDto> updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    List<DiscountDto> getActiveDiscounts(Long productId);
    List<PromotionDto> getPromotionsForProduct(Long productId);
}
