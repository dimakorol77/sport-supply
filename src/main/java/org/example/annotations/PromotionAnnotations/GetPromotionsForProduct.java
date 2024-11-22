package org.example.annotations.PromotionAnnotations;

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
        summary = "Получить акции для продукта",
        description = "Возвращает список акций, связанных с продуктом",
        tags = {"Акции"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Акции получены"),
                @ApiResponse(responseCode = "404", description = "Продукт не найден"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface GetPromotionsForProduct {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/product/{productId}"};
}