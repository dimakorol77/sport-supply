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
    List<ReviewDto> getReviewsByUserId(Long userId, User currentUser);
}
