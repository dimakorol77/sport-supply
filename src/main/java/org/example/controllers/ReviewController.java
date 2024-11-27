package org.example.controllers;

import org.example.dto.ReviewDto;
import org.example.models.User;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.ReviewService;
import org.example.annotations.ReviewAnnotations.GetAllReviews;
import org.example.annotations.ReviewAnnotations.GetReviewById;
import org.example.annotations.ReviewAnnotations.CreateReview;
import org.example.annotations.ReviewAnnotations.UpdateReview;
import org.example.annotations.ReviewAnnotations.DeleteReview;
import org.example.annotations.ReviewAnnotations.GetReviewsByProductId;
import org.example.annotations.ReviewAnnotations.GetReviewsByUserId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@PreAuthorize("isAuthenticated()")
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityUtils securityUtils;


    public ReviewController(ReviewService reviewService, SecurityUtils securityUtils) {
        this.reviewService = reviewService;
        this.securityUtils = securityUtils;
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    @GetAllReviews
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }


    @GetReviewById
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }


    @CreateReview
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto created = reviewService.createReview(reviewDto);
        return ResponseEntity.status(201).body(created);
    }


    @UpdateReview
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto updated = reviewService.updateReview(id, reviewDto);
        return ResponseEntity.ok(updated);
    }


    @DeleteReview
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }


    @GetReviewsByProductId
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }


    @GetReviewsByUserId
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}
