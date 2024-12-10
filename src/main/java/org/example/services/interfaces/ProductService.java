package org.example.services.interfaces;


import org.example.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long id);
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    List<ProductDto> filterAndSortProducts(BigDecimal minPrice, BigDecimal maxPrice, Long categoryId, Boolean hasDiscount, String sortBy, Boolean asc);
}
