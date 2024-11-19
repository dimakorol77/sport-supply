package org.example.exceptions;

public class ProductAlreadyInPromotionException extends RuntimeException {
    public ProductAlreadyInPromotionException(String message) {
        super(message);
    }
}
