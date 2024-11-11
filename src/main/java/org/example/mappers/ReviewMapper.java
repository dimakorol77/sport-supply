package org.example.mappers;

import org.example.dto.ReviewDto;
import org.example.models.Product;
import org.example.models.Review;
import org.example.models.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewDto toDto(Review review) {
        if (review == null) {
            return null;
        }
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUser() != null ? review.getUser().getId() : null);
        dto.setProductId(review.getProduct() != null ? review.getProduct().getId() : null);
        dto.setRating(review.getRating());
        dto.setUserComment(review.getUserComment());
        return dto;
    }

    public Review toEntity(ReviewDto dto) {
        if (dto == null) {
            return null;
        }
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setUserComment(dto.getUserComment());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            review.setUser(user);
        }
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            review.setProduct(product);
        }
        return review;
    }

    public void updateEntityFromDto(ReviewDto dto, Review review) {
        if (dto == null || review == null) {
            return;
        }
        review.setRating(dto.getRating());
        review.setUserComment(dto.getUserComment());
        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            review.setUser(user);
        } else {
            review.setUser(null);
        }
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setId(dto.getProductId());
            review.setProduct(product);
        } else {
            review.setProduct(null);
        }
    }
}
