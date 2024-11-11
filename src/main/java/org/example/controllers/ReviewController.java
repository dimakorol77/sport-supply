package org.example.controllers;

import org.example.dto.ReviewDto;
import org.example.services.interfaces.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // Конструкторная инъекция
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Получить все отзывы
    @GetMapping
    @Operation(summary = "Получение всех отзывов",
            description = "Возвращает список всех отзывов",
            tags = "Отзывы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзывы найдены")
            }
    )
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // Получить отзыв по ID
    @GetMapping("/{id}")
    @Operation(summary = "Получение отзыва по ID",
            description = "Возвращает отзыв с указанным ID",
            tags = "Отзывы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв найден"),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
            }
    )
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        Optional<ReviewDto> reviewOpt = reviewService.getReviewById(id);
        return reviewOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создать новый отзыв
    @PostMapping
    @Operation(summary = "Создание нового отзыва",
            description = "Создает новый отзыв",
            tags = "Отзывы",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Отзыв успешно создан"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto) {
        ReviewDto created = reviewService.createReview(reviewDto);
        return ResponseEntity.status(201).body(created);
    }

    // Обновить отзыв
    @PutMapping("/{id}")
    @Operation(summary = "Обновление отзыва",
            description = "Обновляет отзыв по указанному ID",
            tags = "Отзывы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен"),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDto reviewDto) {
        Optional<ReviewDto> updatedOpt = reviewService.updateReview(id, reviewDto);
        return updatedOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удалить отзыв
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление отзыва",
            description = "Удаляет отзыв по указанному ID",
            tags = "Отзывы",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Отзыв успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
            }
    )
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    // Получить отзывы по ID продукта
    @GetMapping("/product/{productId}")
    @Operation(summary = "Получение отзывов по продукту",
            description = "Возвращает список отзывов для указанного продукта",
            tags = "Отзывы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзывы найдены")
            }
    )
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    // Получить отзывы по ID пользователя
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получение отзывов по пользователю",
            description = "Возвращает список отзывов от указанного пользователя",
            tags = "Отзывы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзывы найдены")
            }
    )
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}
