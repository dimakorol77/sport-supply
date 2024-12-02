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
        summary = "Getting active discounts for a product",
        description = "Returns a list of active discounts for the specified product",
        tags = "Products",
        responses = {
                @ApiResponse(responseCode = "200", description = "Discounts found"),
                @ApiResponse(responseCode = "404", description = "Product not found")
        }
)
public @interface GetActiveDiscounts {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}/discounts"};
}
