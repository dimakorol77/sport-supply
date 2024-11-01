package org.example.services.interfaces;

import org.example.models.Discount;
import org.example.models.Product;
import org.example.models.Promotion;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Optional<Product> updateProduct(Long id, Product updatedProduct);
    void deleteProduct(Long id);
    List<Discount> getActiveDiscounts(Long productId);
    List<Promotion> getPromotionsForProduct(Long productId);
}
