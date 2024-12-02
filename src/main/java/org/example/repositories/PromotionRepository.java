package org.example.repositories;

import org.example.models.Promotion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

  boolean existsByName(String name);
  @EntityGraph(attributePaths = {"productPromotions"})
  List<Promotion> findAll();

}
