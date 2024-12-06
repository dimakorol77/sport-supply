package org.example.exceptions;

public class PaymentAlreadyExistsException extends RuntimeException{
    public PaymentAlreadyExistsException(String message) {
        super(message);
    }
}
