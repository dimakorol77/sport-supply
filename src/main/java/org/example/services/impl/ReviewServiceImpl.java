package org.example.services.impl;

import org.example.dto.ReviewDto;
import org.example.exceptions.ReviewNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.ReviewMapper;
import org.example.models.Review;
import org.example.repositories.ReviewRepository;
import org.example.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::toDto)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorMessage.REVIEW_NOT_FOUND));
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        Review review = reviewMapper.toEntity(reviewDto);
        review.setCreatedAt(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorMessage.REVIEW_NOT_FOUND));

        reviewMapper.updateEntityFromDto(reviewDto, review);
        review.setUpdatedAt(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }

    @Override
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException(ErrorMessage.REVIEW_NOT_FOUND);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    public List<ReviewDto> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }
}
