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
        summary = "Получение отзыва по ID",
        description = "Возвращает отзыв с указанным ID",
        tags = "Отзывы",
        responses = {
                @ApiResponse(responseCode = "200", description = "Отзыв найден"),
                @ApiResponse(responseCode = "404", description = "Отзыв не найден")
        }
)
public @interface GetReviewById {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
