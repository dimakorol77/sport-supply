package org.example.repositories;

import org.example.dto.ProductDto;
import org.example.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // Проверяет, существует ли товар в избранном у пользователя
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    // Получает список избранных товаров пользователя
    List<Favorite> findByUserId(Long userId);

    // Находит избранный товар по пользователю и продукту
    Optional<Favorite> findByUserIdAndProductId(Long userId, Long productId);

    // Удаляет товар из избранного
    void deleteByUserIdAndProductId(Long userId, Long productId);

}
