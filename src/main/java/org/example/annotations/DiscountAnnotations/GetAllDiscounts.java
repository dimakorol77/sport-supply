package org.example.annotations.DiscountAnnotations;

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
        summary = "Getting all discounts",
        description = "Returns a list of all discounts",
        tags = "Discounts",
        responses = {
                @ApiResponse(responseCode = "200", description = "Discounts found")
        }
)
public @interface GetAllDiscounts {

}
