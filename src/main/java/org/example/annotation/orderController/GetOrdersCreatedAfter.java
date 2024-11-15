package org.example.annotation.orderController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получение заказов после даты",
        description = "Возвращает список заказов, созданных после указанной даты. Формат даты: YYYY-MM-DDTHH:MM:SS",
        tags = "Заказы",
        parameters = {
                @Parameter(
                        name = "date",
                        description = "Дата в формате ISO. Пример: 2023-10-01T00:00:00",
                        required = true,
                        in = ParameterIn.QUERY,
                        schema = @Schema(type = "string", format = "date-time", example = "2023-10-01T00:00:00")
                )
        },
        responses = {
                @ApiResponse(responseCode = "200", description = "Список заказов получен"),
                @ApiResponse(responseCode = "400", description = "Некорректная дата")
        }
)
@SecurityRequirement(name = "safety requirements")
public @interface GetOrdersCreatedAfter {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/created-after"};
}
