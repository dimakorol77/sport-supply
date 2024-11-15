package org.example.controllers.handler;

import org.example.exception.*;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.exception.OrderCancellationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.USER_NOT_FOUND);
    }

    // Обработка OrderNotFoundException
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.ORDER_NOT_FOUND);
    }

    // Обработка ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.PRODUCT_NOT_FOUND);
    }

    // Обработка OrderItemNotFoundException
    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<String> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.ORDER_ITEM_NOT_FOUND);
    }
// Обработка OrderCancellationException
    @ExceptionHandler(OrderCancellationException.class)
    public ResponseEntity<String> handleOrderCancellationException(OrderCancellationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.ORDER_CANNOT_BE_CANCELLED);
    }
//Обработка handleCartNotFoundException
@ExceptionHandler(CartNotFoundException.class)
public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.CART_NOT_FOUND);
}
    // Обработка handleCartItemQuantityInvalidException
    @ExceptionHandler(CartItemQuantityInvalidException.class)
    public ResponseEntity<String> handleCartItemQuantityInvalidException(CartItemQuantityInvalidException ex) {
        return new ResponseEntity<>(ErrorMessage.INVALID_QUANTITY, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<String> handlePaymentAlreadyExistsException(PaymentAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.PAYMENT_ALREADY_EXISTS);
    }
// Обработка handleCartItemNotFoundException
@ExceptionHandler(CartItemNotFoundException.class)
public ResponseEntity<String> handleCartItemNotFoundException(CartItemNotFoundException ex) {
    return new ResponseEntity<>(ErrorMessage.CART_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
}

//стандартное исключени Spring
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.INVALID_DATE_FORMAT);
    }

    // Обработка PaymentNotFoundException
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessage.PAYMENT_NOT_FOUND);
    }

    // Обработка исключения CartAlreadyExistsException
    @ExceptionHandler(CartAlreadyExistsException.class)
    public ResponseEntity<String> handleCartAlreadyExistsException(CartAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.CART_ALREADY_EXISTS);
    }
    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<String> handleCartEmptyException(CartEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.CART_EMPTY);
    }


    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantityException(InvalidQuantityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.INVALID_QUANTITY);
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



    @ExceptionHandler(FavoriteAlreadyExistsException.class)
    public ResponseEntity<String> handleFavoriteAlreadyExistsException(FavoriteAlreadyExistsException ex) {
        return new ResponseEntity<>(ErrorMessage.FAVORITE_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FavoriteNotFoundException.class)
    public ResponseEntity<String> handleFavoriteNotFoundException(FavoriteNotFoundException ex) {
        return new ResponseEntity<>(ErrorMessage.FAVORITE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
