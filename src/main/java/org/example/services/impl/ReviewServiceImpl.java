package org.example.services.impl;

import org.example.models.Review;
import org.example.repositories.ReviewRepository;
import org.example.services.interfaces.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    // Используем конструкторную инъекцию
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Получить все отзывы
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Получить отзыв по ID
    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Создать новый отзыв
    @Override
    public Review createReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    // Обновить отзыв
    @Override
    public Optional<Review> updateReview(Long id, Review updatedReview) {
        return reviewRepository.findById(id).map(review -> {
            review.setRating(updatedReview.getRating());
            review.setUserComment(updatedReview.getUserComment());
            review.setUpdatedAt(LocalDateTime.now());
            // Обновление других полей при необходимости
            return reviewRepository.save(review);
        });
    }

    // Удалить отзыв
    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    // Получить отзывы по ID продукта
    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    // Получить отзывы по ID пользователя
    @Override
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}
