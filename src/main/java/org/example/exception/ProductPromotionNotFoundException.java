package org.example.exception;

public class ProductPromotionNotFoundException extends RuntimeException {
    public ProductPromotionNotFoundException(String message) {
        super(message);
    }
}
