package org.example.controllers.handler;

import org.example.exception.*;
import org.example.exception.errorMessage.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ResponseExceptionHandler {
    // Обработка IdNotFoundException
    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.ID_NOT_FOUND);
    }

    // Обработка UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.USER_ALREADY_EXISTS);
    }

    // Обработка UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.USER_NOT_FOUND);
    }

    // Обработка OrderNotFoundException
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.ORDER_NOT_FOUND);
    }

    // Обработка ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.PRODUCT_NOT_FOUND);
    }

    // Обработка OrderItemNotFoundException
    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.ORDER_ITEM_NOT_FOUND);
    }

    // Обработка OrderCancellationException
    @ExceptionHandler(OrderCancellationException.class)
    public ResponseEntity<String> handleOrderCancellationException(OrderCancellationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.ORDER_CANNOT_BE_CANCELLED);
    }

    // Обработка CartNotFoundException
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.CART_NOT_FOUND);
    }

    // Обработка CartItemQuantityInvalidException
    @ExceptionHandler(CartItemQuantityInvalidException.class)
    public ResponseEntity<String> handleCartItemQuantityInvalidException(CartItemQuantityInvalidException ex) {
        return new ResponseEntity<>(ErrorMessage.INVALID_QUANTITY, HttpStatus.BAD_REQUEST);
    }

    // Обработка PaymentAlreadyExistsException
    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<String> handlePaymentAlreadyExistsException(PaymentAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.PAYMENT_ALREADY_EXISTS);
    }

    // Обработка CartItemNotFoundException
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFoundException(CartItemNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.CART_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка методного аргумента типа
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.INVALID_DATE_FORMAT);
    }

    // Обработка PaymentNotFoundException
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.PAYMENT_NOT_FOUND);
    }

    // Обработка CartAlreadyExistsException
    @ExceptionHandler(CartAlreadyExistsException.class)
    public ResponseEntity<String> handleCartAlreadyExistsException(CartAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.CART_ALREADY_EXISTS);
    }

    // Обработка CartEmptyException
    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<String> handleCartEmptyException(CartEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.CART_EMPTY);
    }

    // Обработка InvalidQuantityException
    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantityException(InvalidQuantityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.INVALID_QUANTITY);
    }

    // Обработка ReviewNotFoundException
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> handleReviewNotFoundException(ReviewNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.REVIEW_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка BrandNotFoundException
    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<String> handleBrandNotFoundException(BrandNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка BrandAlreadyExistsException
    @ExceptionHandler(BrandAlreadyExistsException.class)
    public ResponseEntity<String> handleBrandAlreadyExistsException(BrandAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorMessage.BRAND_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    // Обработка CategoryNotFoundException
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка CategoryAlreadyExistsException
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<String> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorMessage.CATEGORY_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    // Обработка DiscountNotFoundException
    @ExceptionHandler(DiscountNotFoundException.class)
    public ResponseEntity<String> handleDiscountNotFoundException(DiscountNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.DISCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка ImageNotFoundException
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<String> handleImageNotFoundException(ImageNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.IMAGE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка ImageUploadException
    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<String> handleImageUploadException(ImageUploadException ex) {
        return new ResponseEntity<>(ErrorMessage.IMAGE_UPLOAD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Обработка ProductAlreadyExistsException
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorMessage.PRODUCT_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    // Обработка ProductAlreadyInPromotionException
    @ExceptionHandler(ProductAlreadyInPromotionException.class)
    public ResponseEntity<String> handleProductAlreadyInPromotionException(ProductAlreadyInPromotionException ex) {
        return new ResponseEntity<>(ErrorMessage.PRODUCT_ALREADY_IN_PROMOTION, HttpStatus.CONFLICT);
    }

    // Обработка ProductPromotionNotFoundException
    @ExceptionHandler(ProductPromotionNotFoundException.class)
    public ResponseEntity<String> handleProductPromotionNotFoundException(ProductPromotionNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.PRODUCT_PROMOTION_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка FavoriteAlreadyExistsException
    @ExceptionHandler(FavoriteAlreadyExistsException.class)
    public ResponseEntity<String> handleFavoriteAlreadyExistsException(FavoriteAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorMessage.FAVORITE_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    // Обработка FavoriteNotFoundException
    @ExceptionHandler(FavoriteNotFoundException.class)
    public ResponseEntity<String> handleFavoriteNotFoundException(FavoriteNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.FAVORITE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    // Обработка ошибок валидации
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder(ErrorMessage.VALIDATION_ERROR);
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }

    // Общий обработчик для всех остальных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.GENERIC_ERROR);
    }
}
