package org.example.repositories;

import org.example.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
//поиск активных акций?
    List<Promotion> findByStartDateBeforeAndEndDateAfter(Date startDate, Date endDate);
}
