package org.example.exception.errorMessage;

public class ErrorMessage {
    public static final String ID_NOT_FOUND = "This id was not found";
    public static final String USER_ALREADY_EXISTS = "User with this email already exists";



    public static final String EMAIL_FORMAT_INVALID = "Invalid email format";
    public static final String NAME_NOT_EMPTY = "Name cannot be empty";
    public static final String PASSWORD_NOT_EMPTY = "Password cannot be empty";
    public static final String INVALID_USER_DATA = "Invalid user data";

    public static final String VALIDATION_ERROR = "Validation error: "; // Сообщение для валидации
    public static final String GENERIC_ERROR = "An unexpected error occurred"; // Общее сообщение об ошибке


    //для orders
    public static final String ORDER_NOT_FOUND = "Order not found"; // Заказ не найден
    public static final String INVALID_ORDER_DATA = "Invalid order data"; // Неверные данные заказа
    public static final String USER_NOT_FOUND = "User not found"; // Пользователь не найден
    public static final String PRODUCT_NOT_FOUND = "Product not found.";
    public static final String ORDER_ITEM_NOT_FOUND = "Order item not found";
}
