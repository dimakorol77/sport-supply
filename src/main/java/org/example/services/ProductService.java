package org.example.services;

import org.example.models.Discount;
import org.example.models.Product;
import org.example.models.ProductPromotion;
import org.example.models.Promotion;
import org.example.repositories.DiscountRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductPromotionRepository productPromotionRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    // Получить все продукты
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Получить продукт по ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Создать новый продукт
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Обновить продукт
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setCategory(updatedProduct.getCategory());
            product.setBrand(updatedProduct.getBrand());
            product.setProteinType(updatedProduct.getProteinType());
            product.setVitaminGroup(updatedProduct.getVitaminGroup());
            product.setForm(updatedProduct.getForm());
            // Обновление других полей при необходимости
            return productRepository.save(product);
        });
    }

    // Удалить продукт
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Получить активные скидки для продукта
    public List<Discount> getActiveDiscounts(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByProductIdAndStartDateBeforeAndEndDateAfter(productId, now, now);
    }

    // Получить акции для продукта
    public List<Promotion> getPromotionsForProduct(Long productId) {
        List<ProductPromotion> productPromotions = productPromotionRepository.findByProductId(productId);
        return productPromotions.stream()
                .map(ProductPromotion::getPromotion)
                .collect(Collectors.toList());
    }
}
