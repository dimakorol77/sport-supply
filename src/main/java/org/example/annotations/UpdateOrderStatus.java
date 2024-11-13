package org.example.annotations;

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
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Обновление статуса заказа",
        description = "Обновляет статус указанного заказа",
        tags = {"Заказы"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Статус заказа успешно обновлен"),
                @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                @ApiResponse(responseCode = "404", description = "Заказ не найден")
        }
)
public @interface UpdateOrderStatus {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderId}/status"};
}
