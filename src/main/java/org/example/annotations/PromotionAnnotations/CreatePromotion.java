package org.example.annotations.PromotionAnnotations;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Создание новой акции",
        description = "Создает новую акцию",
        tags = "Акции",
        responses = {
                @ApiResponse(responseCode = "201", description = "Акция успешно создана"),
                @ApiResponse(responseCode = "400", description = "Некорректные данные")
        }
)
public @interface CreatePromotion {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/"};
}
