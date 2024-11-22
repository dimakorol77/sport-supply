package org.example.annotations.PromotionAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Добавить продукт в акцию",
        description = "Добавляет продукт в акцию",
        tags = {"Акции"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Продукт добавлен в акцию"),
                @ApiResponse(responseCode = "404", description = "Акция или продукт не найдены"),
                @ApiResponse(responseCode = "409", description = "Продукт уже находится в акции"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface AddProductToPromotion {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{promotionId}/products/{productId}"};
}