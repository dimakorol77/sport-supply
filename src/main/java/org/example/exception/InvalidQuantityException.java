package org.example.exception;

public class InvalidQuantityException extends RuntimeException{
    public InvalidQuantityException(String message) {
        super(message);
    }
}
