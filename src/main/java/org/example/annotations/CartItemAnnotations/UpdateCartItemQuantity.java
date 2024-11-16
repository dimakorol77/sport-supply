package org.example.annotations.CartItemAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Обновление количества товара в корзине",
        description = "Обновляет количество товара в корзине",
        tags = "Корзина",
        responses = {
                @ApiResponse(responseCode = "200", description = "Количество товара обновлено"),
                @ApiResponse(responseCode = "404", description = "Товар в корзине не найден")
        }
)
public @interface UpdateCartItemQuantity {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/items/{cartItemId}"};
}