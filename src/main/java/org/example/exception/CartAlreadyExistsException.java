package org.example.exception;

public class CartAlreadyExistsException extends RuntimeException{
    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
