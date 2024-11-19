package org.example.exceptions;

public class CartEmptyException extends RuntimeException{
    public CartEmptyException(String message) {
        super(message);
    }
}

