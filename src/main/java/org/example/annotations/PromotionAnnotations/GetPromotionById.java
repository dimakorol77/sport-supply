package org.example.annotations.PromotionAnnotations;


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
        summary = "Получение акции по ID",
        description = "Возвращает акцию с указанным ID",
        tags = "Акции",
        responses = {
                @ApiResponse(responseCode = "200", description = "Акция найдена"),
                @ApiResponse(responseCode = "404", description = "Акция не найдена")
        }
)
public @interface GetPromotionById {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
