package org.example.exceptions;

public class FavoriteNotFoundException extends RuntimeException{
    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
