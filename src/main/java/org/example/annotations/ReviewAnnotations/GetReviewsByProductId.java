package org.example.annotations.ReviewAnnotations;


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
        summary = "Получение отзывов по продукту",
        description = "Возвращает список отзывов для указанного продукта",
        tags = "Отзывы",
        responses = {
                @ApiResponse(responseCode = "200", description = "Отзывы найдены")
        }
)
public @interface GetReviewsByProductId {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/product/{productId}"};
}
