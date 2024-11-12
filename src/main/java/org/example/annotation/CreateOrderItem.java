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
        summary = "Создание элемента заказа",
        description = "Создает новый элемент для указанного заказа",
        tags = {"Элементы заказа"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Элемент заказа успешно создан"),
                @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                @ApiResponse(responseCode = "404", description = "Заказ не найден")
        }
)
public @interface CreateOrderItem {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderId}"};
}
