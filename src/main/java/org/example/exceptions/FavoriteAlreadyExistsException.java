package org.example.exceptions;

public class FavoriteAlreadyExistsException extends RuntimeException{
    public FavoriteAlreadyExistsException(String message) {
        super(message);
    }
}
