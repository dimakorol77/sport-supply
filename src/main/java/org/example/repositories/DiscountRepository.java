package org.example.repositories;

import org.example.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findByProductIdAndStartDateBeforeAndEndDateAfter(Long productId, LocalDateTime startDate, LocalDateTime endDate);
}
