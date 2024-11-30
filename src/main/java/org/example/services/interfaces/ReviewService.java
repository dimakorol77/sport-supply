package org.example.services.interfaces;

import org.example.dto.ReviewDto;
import org.example.models.User;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getAllReviews();
    ReviewDto getReviewById(Long id);
    ReviewDto createReview(ReviewDto reviewDto);
    ReviewDto updateReview(Long id, ReviewDto reviewDto);
    void deleteReview(Long id);
    List<ReviewDto> getReviewsByProductId(Long productId);

    /**
     * Получить отзывы пользователя.
     * Доступ только для самого пользователя или администратора.
     *
     * @param userId      ID пользователя
     * @param currentUser текущий пользователь (аутентифицированный или администратор)
     * @return список отзывов пользователя
     */
    List<ReviewDto> getReviewsByUserId(Long userId, User currentUser);
}
