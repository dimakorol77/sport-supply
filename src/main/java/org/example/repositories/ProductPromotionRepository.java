package org.example.repositories;

import org.example.models.ProductPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, Long> {
    //поиск всех акций для конкретн продукта
    List<ProductPromotion> findByProductId(Long productId);

    //поиск всех продуктов для конкретной акции
    List<ProductPromotion> findByPromotionId(Long promotionId);
}
