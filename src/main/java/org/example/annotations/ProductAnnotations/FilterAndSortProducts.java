package org.example.annotations.ProductAnnotations;

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
        summary = "Filtering and sorting products",
        description = "Allows filtering and sorting products by various criteria",
        tags = {"Products"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Products retrieved"),
                @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface FilterAndSortProducts {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/filter"};
}
