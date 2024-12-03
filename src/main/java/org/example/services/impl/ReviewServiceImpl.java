package org.example.services.impl;

import org.example.dto.ReviewDto;
import org.example.enums.Role;
import org.example.exceptions.ReviewNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.ReviewMapper;
import org.example.models.Review;
import org.example.models.User;
import org.example.repositories.ReviewRepository;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final SecurityUtils securityUtils;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ReviewMapper reviewMapper,
                             SecurityUtils securityUtils) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.securityUtils = securityUtils;
    }

    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorMessage.REVIEW_NOT_FOUND));

        User currentUser = getCurrentUser();

        if (!currentUser.getRole().equals(Role.ADMIN) && !review.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {
        User currentUser = getCurrentUser();
        Review review = reviewMapper.toEntity(reviewDto);
        review.setUser(currentUser);
        review.setCreatedAt(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long id, ReviewDto reviewDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorMessage.REVIEW_NOT_FOUND));

        User currentUser = getCurrentUser();

        if (!review.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        reviewMapper.updateEntityFromDto(reviewDto, review);

        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toDto(updatedReview);
    }


    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(ErrorMessage.REVIEW_NOT_FOUND));

        User currentUser = getCurrentUser();

        if (!review.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDto> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> getReviewsByUserId(Long userId) {
        User currentUser = getCurrentUser();

        if (!userId.equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        return reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }
}

