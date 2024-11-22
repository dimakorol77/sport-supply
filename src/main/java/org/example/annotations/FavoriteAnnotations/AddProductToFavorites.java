package org.example.annotations.FavoriteAnnotations;

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
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Добавление товара в избранное",
        description = "Добавляет товар в избранное пользователя",
        tags = {"Избранное"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Товар добавлен в избранное"),
                @ApiResponse(responseCode = "404", description = "Пользователь или товар не найдены"),
                @ApiResponse(responseCode = "409", description = "Товар уже в избранном"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface AddProductToFavorites {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/add/{productId}"};
}