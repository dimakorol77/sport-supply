package org.example.annotations.DiscountAnnotations;

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
        summary = "Получение скидки по ID",
        description = "Возвращает скидку с указанным ID",
        tags = "Скидки",
        responses = {
                @ApiResponse(responseCode = "200", description = "Скидка найдена"),
                @ApiResponse(responseCode = "404", description = "Скидка не найдена")
        }
)
public @interface GetDiscountById {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
