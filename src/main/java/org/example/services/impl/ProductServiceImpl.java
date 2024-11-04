package org.example.services.impl;

import org.example.models.Discount;
import org.example.models.Product;
import org.example.models.ProductPromotion;
import org.example.models.Promotion;
import org.example.repositories.DiscountRepository;
import org.example.repositories.ProductPromotionRepository;
import org.example.repositories.ProductRepository;
import org.example.repositories.PromotionRepository;
import org.example.services.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final PromotionRepository promotionRepository;

    // Используем конструкторную инъекцию
    public ProductServiceImpl(ProductRepository productRepository,
                              DiscountRepository discountRepository,
                              ProductPromotionRepository productPromotionRepository,
                              PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.promotionRepository = promotionRepository;
    }

    // Получить все продукты
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Получить продукт по ID
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Создать новый продукт
    @Override
    public Product createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    // Обновить продукт
    @Override
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
            product.setUpdatedAt(LocalDateTime.now());
            // Обновление других полей при необходимости
            return productRepository.save(product);
        });
    }

    // Удалить продукт
    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Получить активные скидки для продукта
    @Override
    public List<Discount> getActiveDiscounts(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return discountRepository.findByProductIdAndStartDateBeforeAndEndDateAfter(productId, now, now);
    }

    // Получить акции для продукта
    @Override
    public List<Promotion> getPromotionsForProduct(Long productId) {
        List<ProductPromotion> productPromotions = productPromotionRepository.findByProductId(productId);
        return productPromotions.stream()
                .map(ProductPromotion::getPromotion)
                .collect(Collectors.toList());
    }
}
