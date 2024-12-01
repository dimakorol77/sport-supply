package org.example.annotations.FavoriteAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получить избранные товары пользователя (только для администратора)",
        description = "Администратор может получить список избранных товаров любого пользователя по его ID",
        tags = {"Избранное"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Список избранных товаров получен"),
                @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден")
        }
)
public @interface GetUserFavoritesByAdmin {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{userId}"};
}
