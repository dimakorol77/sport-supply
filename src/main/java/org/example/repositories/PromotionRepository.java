package org.example.repositories;

import org.example.models.Promotion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
  //  List<Promotion> findByStartDateBeforeAndEndDateAfter(LocalDateTime startDate, LocalDateTime endDate);
  boolean existsByName(String name);
  @EntityGraph(attributePaths = {"productPromotions"})
  List<Promotion> findAll();

}
