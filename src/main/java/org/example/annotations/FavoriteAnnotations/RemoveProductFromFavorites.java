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
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Удаление товара из избранного",
        description = "Удаляет товар из избранного пользователя",
        tags = {"Избранное"},
        responses = {
                @ApiResponse(responseCode = "204", description = "Товар удален из избранного"),
                @ApiResponse(responseCode = "404", description = "Товар не найден в избранном"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface RemoveProductFromFavorites {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/remove/{productId}"};
}
