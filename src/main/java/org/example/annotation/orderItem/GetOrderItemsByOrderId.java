package org.example.annotation.orderItem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.GetMapping;
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
        summary = "Получение элементов заказа",
        description = "Возвращает все элементы для указанного заказа",
        tags = {"Элементы заказа"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Список элементов заказа успешно возвращен"),
                @ApiResponse(responseCode = "404", description = "Заказ не найден")
        }
)
public @interface GetOrderItemsByOrderId {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderId}"};
}
