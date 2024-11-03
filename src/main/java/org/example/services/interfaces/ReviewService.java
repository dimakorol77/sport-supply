package org.example.services.interfaces;

import org.example.models.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();
    Optional<Review> getReviewById(Long id);
    Review createReview(Review review);
    Optional<Review> updateReview(Long id, Review updatedReview);
    void deleteReview(Long id);
    List<Review> getReviewsByProductId(Long productId);
    List<Review> getReviewsByUserId(Long userId);
}
