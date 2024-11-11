package org.example.services.interfaces;

import org.example.dto.ReviewDto;
import org.example.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<ReviewDto> getAllReviews();
    Optional<ReviewDto> getReviewById(Long id);
    ReviewDto createReview(ReviewDto reviewDto);
    Optional<ReviewDto> updateReview(Long id, ReviewDto reviewDto);
    void deleteReview(Long id);
    List<ReviewDto> getReviewsByProductId(Long productId);
    List<ReviewDto> getReviewsByUserId(Long userId);
}

