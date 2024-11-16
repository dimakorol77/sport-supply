package org.example.annotations.OrderAnnotations;

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
        summary = "Получение заказов по ID пользователя",
        description = "Возвращает список заказов для указанного пользователя",
        tags = {"Заказы"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Заказы найдены"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден")
        }
)
public @interface GetOrdersByUserId {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/user/{userId}"};
}
