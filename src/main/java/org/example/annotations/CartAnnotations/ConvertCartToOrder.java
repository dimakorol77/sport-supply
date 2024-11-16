package org.example.annotations.CartAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Конвертация корзины в заказ",
        description = "Конвертирует корзину в заказ и обновляет товары в корзине",
        tags = "Корзина",
        responses = {
                @ApiResponse(responseCode = "201", description = "Заказ успешно создан"),
                @ApiResponse(responseCode = "400", description = "Корзина пуста или неверные данные запроса"),
                @ApiResponse(responseCode = "404", description = "Корзина не найдена")
        }
)
public @interface ConvertCartToOrder {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/convert/{cartId}"};
}
