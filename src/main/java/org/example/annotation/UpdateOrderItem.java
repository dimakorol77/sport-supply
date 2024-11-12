package org.example.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Обновление элемента заказа",
        description = "Обновляет данные указанного элемента заказа",
        tags = {"Элементы заказа"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Элемент заказа успешно обновлен"),
                @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                @ApiResponse(responseCode = "404", description = "Элемент заказа не найден")
        }
)
public @interface UpdateOrderItem {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderItemId}"};
}