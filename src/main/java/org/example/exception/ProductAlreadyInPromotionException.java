package org.example.exception;

public class ProductAlreadyInPromotionException extends RuntimeException {
    public ProductAlreadyInPromotionException(String message) {
        super(message);
    }
}
