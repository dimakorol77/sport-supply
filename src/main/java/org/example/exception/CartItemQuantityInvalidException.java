package org.example.exception;

public class CartItemQuantityInvalidException extends RuntimeException{
    public CartItemQuantityInvalidException(String message) {
        super(message);
    }
}
