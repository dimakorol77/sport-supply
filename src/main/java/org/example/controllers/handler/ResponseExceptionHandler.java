package org.example.controllers.handler;

import org.example.exception.IdNotFoundException;
import org.example.exception.OrderNotFoundException;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.UserNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
