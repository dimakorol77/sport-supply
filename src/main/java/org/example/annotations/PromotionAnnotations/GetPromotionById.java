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
        summary = "Getting a promotion by ID",
        description = "Returns the promotion with the specified ID",
        tags = "Promotions",
        responses = {
                @ApiResponse(responseCode = "200", description = "Promotion found"),
                @ApiResponse(responseCode = "404", description = "Promotion not found")
        }
)
public @interface GetPromotionById {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
