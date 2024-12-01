package org.example.exceptions;

public class PromotionDuplicateEntityException extends RuntimeException {
    public PromotionDuplicateEntityException(String message) {
        super(message);
    }
}
