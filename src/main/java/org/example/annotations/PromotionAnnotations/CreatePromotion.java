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
        summary = "Creating a new promotion",
        description = "Creates a new promotion",
        tags = "Promotions",
        responses = {
                @ApiResponse(responseCode = "201", description = "Promotion successfully created"),
                @ApiResponse(responseCode = "400", description = "Invalid data")
        }
)
public @interface CreatePromotion {

}
