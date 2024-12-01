package org.example.annotations.PromotionAnnotations;

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
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Удалить продукт из акции",
        description = "Удаляет продукт из акции",
        tags = {"Акции"},
        responses = {
                @ApiResponse(responseCode = "204", description = "Продукт удален из акции"),
                @ApiResponse(responseCode = "404", description = "Акция или продукт не найдены"),
                @ApiResponse(responseCode = "403", description = "У вас нет доступа")
        }
)
public @interface RemoveProductFromPromotion {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{promotionId}/products/{productId}"};
}