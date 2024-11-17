package org.example.repositories;

import org.example.models.ProductPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPromotionRepository extends JpaRepository<ProductPromotion, Long> {

    List<ProductPromotion> findByProductId(Long productId);

    List<ProductPromotion> findByPromotionId(Long promotionId);

    Optional<ProductPromotion> findByProductIdAndPromotionId(Long productId, Long promotionId);
}
