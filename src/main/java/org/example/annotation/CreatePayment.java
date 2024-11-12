package org.example.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.PostMapping;
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
        summary = "Создание нового платежа",
        description = "Создает новый платеж для заказа",
        tags = {"Платежи"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Платеж успешно создан"),
                @ApiResponse(responseCode = "404", description = "Заказ не найден")
        }
)
public @interface CreatePayment {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/create"};
}
