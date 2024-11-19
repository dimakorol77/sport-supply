package org.example.exceptions.errorMessage;

public class ErrorMessage {
    public static final String ID_NOT_FOUND = "This id was not found";
    public static final String USER_ALREADY_EXISTS = "User with this email already exists";

    public static final String EMAIL_FORMAT_INVALID = "Invalid email format";
    public static final String NAME_NOT_EMPTY = "Name cannot be empty";
    public static final String PASSWORD_NOT_EMPTY = "Password cannot be empty";
    public static final String INVALID_USER_DATA = "Invalid user data";

    public static final String VALIDATION_ERROR = "Validation error: "; // Сообщение для валидации
    public static final String GENERIC_ERROR = "An unexpected error occurred"; // Общее сообщение об ошибке

    // Для Orders
    public static final String ORDER_NOT_FOUND = "Order not found"; // Заказ не найден
    public static final String INVALID_ORDER_DATA = "Invalid order data"; // Неверные данные заказа
    public static final String USER_NOT_FOUND = "User not found"; // Пользователь не найден
    public static final String PRODUCT_NOT_FOUND = "Product not found.";
    public static final String ORDER_ITEM_NOT_FOUND = "Order item not found";
    public static final String ORDER_CANNOT_BE_CANCELLED = "Order cannot be cancelled in its current status";

    // Для Cart
    public static final String CART_NOT_FOUND = "Cart not found";
    public static final String CART_ITEM_PRODUCT_REQUIRED = "Product is required to add an item to the cart";
    public static final String CART_ITEM_QUANTITY_REQUIRED = "Quantity is required for the cart item";
    public static final String CART_ITEM_QUANTITY_POSITIVE = "Quantity must be positive";
    public static final String CART_ITEM_NOT_FOUND = "Product not found in cart";
    public static final String CART_ITEM_USER_REQUIRED = "User for the cart is required.";
    public static final String CART_ITEM_CREATED_AT_REQUIRED = "Creation date of the cart is required.";
    public static final String INVALID_QUANTITY = "Quantity must be greater than 0";
    public static final String CART_ALREADY_EXISTS = "Cart already exists";
    public static final String CART_EMPTY = "Cart is empty, cannot create an order";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Expected format: YYYY-MM-DDTHH:MM:SS";

    public static final String PAYMENT_NOT_FOUND = "Payment not found";
    public static final String PAYMENT_ALREADY_EXISTS = "A payment for this order already exists.";

    public static final String FAVORITE_ALREADY_EXISTS = "Favorite already exists";
    public static final String FAVORITE_NOT_FOUND = "Favorite not found";

    public static final String PROMOTION_NOT_FOUND = "Promotion not found";

    // Новые сообщения об ошибках
    // Для Brand
    public static final String BRAND_NOT_FOUND = "Brand not found";
    public static final String BRAND_ALREADY_EXISTS = "Brand already exists";

    // Для Category
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String CATEGORY_ALREADY_EXISTS = "Category already exists";

    // Для Discount
    public static final String DISCOUNT_NOT_FOUND = "Discount not found";

    // Для Image
    public static final String IMAGE_NOT_FOUND = "Image not found";
    public static final String IMAGE_UPLOAD_FAILED = "Image upload failed";

    // Для Product
    public static final String PRODUCT_ALREADY_EXISTS = "Product already exists";

    // Для Promotion
    public static final String PRODUCT_ALREADY_IN_PROMOTION = "Product is already in this promotion";
    public static final String PRODUCT_PROMOTION_NOT_FOUND = "ProductPromotion not found for the given product and promotion";

    // Для Review
    public static final String REVIEW_NOT_FOUND = "Review not found";

    // Для других возможных ошибок
    public static final String INVALID_SORT_FIELD = "Invalid sort field: ";
}
