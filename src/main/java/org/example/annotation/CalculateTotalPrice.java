package org.example.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Подсчет общей стоимости",
        description = "Возвращает общую стоимость товаров в корзине",
        tags = "Корзина",
        responses = {
                @ApiResponse(responseCode = "200", description = "Общая стоимость вычислена"),
                @ApiResponse(responseCode = "404", description = "Корзина не найдена")
        }
)
public @interface CalculateTotalPrice {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{cartId}/total"};
}