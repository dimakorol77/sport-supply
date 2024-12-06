package org.example.exceptions;

public class ProductPromotionNotFoundException extends RuntimeException {
    public ProductPromotionNotFoundException(String message) {
        super(message);
    }
}
