package org.example.exceptions;

public class OrderCancellationException extends RuntimeException{
    public OrderCancellationException(String message) {
        super(message);
    }
}
