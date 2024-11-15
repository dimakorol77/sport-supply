package org.example.annotation.orderController;

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
        summary = "Получение заказов по статусу",
        description = "Возвращает список заказов с указанным статусом",
        tags = "Заказы",
        responses = {
                @ApiResponse(responseCode = "200", description = "Список заказов получен"),
                @ApiResponse(responseCode = "400", description = "Некорректный статус")
        }
)
public @interface GetOrdersByStatus {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/status"};
}
