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
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получение списка избранных товаров пользователя",
        description = "Возвращает список избранных товаров для указанного пользователя",
        tags = {"Избранное"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Список избранных товаров получен"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден")
        }
)
public @interface GetUserFavorites {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{userId}"};
}