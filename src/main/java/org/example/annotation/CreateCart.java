package org.example.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
        summary = "Создание новой корзины",
        description = "Создает новую корзину для пользователя",
        tags = "Корзина",
        responses = {
                @ApiResponse(responseCode = "201", description = "Корзина успешно создана"),
                @ApiResponse(responseCode = "404", description = "Пользователь не найден")
        }
)
@SecurityRequirement(name = "safety requirements")
public @interface CreateCart {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{userId}/create"};
}