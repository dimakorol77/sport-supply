package org.example.controllers.handler;

import org.example.exception.*;
import org.example.exception.errorMessage.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseExceptionHandler {
    // Обработка IdNotFoundException
    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.ID_NOT_FOUND);
    }

    // Обработка UserAlreadyExistsException
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.USER_ALREADY_EXISTS);
    }



    // Обработка UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Обработка OrderNotFoundException
        @ExceptionHandler(OrderNotFoundException.class)
        public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

    // Обработка ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.PRODUCT_NOT_FOUND);
    }

    // Обработка OrderItemNotFoundException
    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.ORDER_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

//Обработка handleCartNotFoundException
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    // Обработка handleCartItemQuantityInvalidException
    @ExceptionHandler(CartItemQuantityInvalidException.class)
    public ResponseEntity<String> handleCartItemQuantityInvalidException(CartItemQuantityInvalidException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
// Обработка handleCartItemNotFoundException
    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFoundException(CartItemNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantityException(InvalidQuantityException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    // Обработка ошибок валидации
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder(ErrorMessage.VALIDATION_ERROR);
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }

    // Общий обработчик для всех остальных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.GENERIC_ERROR);
    }
}
