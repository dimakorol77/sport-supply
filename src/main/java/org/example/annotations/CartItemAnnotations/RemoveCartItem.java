package org.example.annotations.CartItemAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Удаление товара из корзины",
        description = "Удаляет товар из корзины",
        tags = "Корзина",
        responses = {
                @ApiResponse(responseCode = "204", description = "Товар удален из корзины"),
                @ApiResponse(responseCode = "404", description = "Товар в корзине не найден")
        }
)
public @interface RemoveCartItem {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/items/{cartItemId}"};
}