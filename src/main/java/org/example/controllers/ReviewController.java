package org.example.controllers;

import org.example.dto.ReviewDto;
import org.example.services.interfaces.ReviewService;
import org.example.annotations.ReviewAnnotations.GetAllReviews;
import org.example.annotations.ReviewAnnotations.GetReviewById;
import org.example.annotations.ReviewAnnotations.CreateReview;
import org.example.annotations.ReviewAnnotations.UpdateReview;
import org.example.annotations.ReviewAnnotations.DeleteReview;
import org.example.annotations.ReviewAnnotations.GetReviewsByProductId;
import org.example.annotations.ReviewAnnotations.GetReviewsByUserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // Конструкторная инъекция
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Получить все отзывы
    @GetAllReviews
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // Получить отзыв по ID
    @GetReviewById
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    // Создать новый отзыв
    @CreateReview
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto created = reviewService.createReview(reviewDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить отзыв
    @UpdateReview
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto updated = reviewService.updateReview(id, reviewDto);
        return ResponseEntity.ok(updated);
    }

    // Удалить отзыв
    @DeleteReview
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // Получить отзывы по ID продукта
    @GetReviewsByProductId
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    // Получить отзывы по ID пользователя
    @GetReviewsByUserId
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}
