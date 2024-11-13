package org.example.annotations.ProductAnnotations;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Получение активных скидок для продукта",
        description = "Возвращает список активных скидок для указанного продукта",
        tags = "Продукты",
        responses = {
                @ApiResponse(responseCode = "200", description = "Скидки найдены"),
                @ApiResponse(responseCode = "404", description = "Продукт не найден")
        }
)
public @interface GetActiveDiscounts {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}/discounts"};
}
