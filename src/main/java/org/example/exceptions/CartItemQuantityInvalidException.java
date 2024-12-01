package org.example.exceptions;

public class CartItemQuantityInvalidException extends RuntimeException{
    public CartItemQuantityInvalidException(String message) {
        super(message);
    }
}
