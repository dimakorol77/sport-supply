package org.example.annotations.CartItemAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Добавление товара в корзину",
        description = "Добавляет новый товар в корзину",
        tags = "Корзина",
        responses = {
                @ApiResponse(responseCode = "201", description = "Товар успешно добавлен в корзину"),
                @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
                @ApiResponse(responseCode = "404", description = "Корзина не найдена")
        }
)
public @interface AddItemToCart {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{cartId}/items"};
}