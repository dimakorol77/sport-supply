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
        summary = "Add a product to a promotion",
        description = "Adds a product to a promotion",
        tags = {"Promotions"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Product added to the promotion"),
                @ApiResponse(responseCode = "404", description = "Promotion or product not found"),
                @ApiResponse(responseCode = "409", description = "Product is already in the promotion"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface AddProductToPromotion {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{promotionId}/products/{productId}"};
}