package org.example.repositories;

import org.example.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByProductIdAndStartDateBeforeAndEndDateAfter(Long productId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Discount> findFirstByProductIdAndStartDateBeforeAndEndDateAfterOrderByDiscountPriceDesc(Long productId, LocalDateTime startDate, LocalDateTime endDate);
}
