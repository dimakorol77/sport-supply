package org.example.annotations.DiscountAnnotations;



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
        summary = "Creating a new discount",
        description = "Creates a new discount",
        tags = "Discounts",
        responses = {
                @ApiResponse(responseCode = "201", description = "Discount successfully created"),
                @ApiResponse(responseCode = "400", description = "Incorrect data")
        }
)
public @interface CreateDiscount {

}
