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
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Getting promotions for a product",
        description = "Returns a list of promotions associated with the product",
        tags = {"Promotions"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Promotions retrieved"),
                @ApiResponse(responseCode = "404", description = "Product not found"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface GetPromotionsForProduct {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/product/{productId}"};
}