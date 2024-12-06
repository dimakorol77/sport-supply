package org.example.repositories;

import org.example.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long productId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Discount> findFirstByProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByDiscountPriceDesc(Long productId, LocalDateTime startDate, LocalDateTime endDate);
}
