package org.example.annotations.DiscountAnnotations;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Update a discount",
        description = "Updates the discount with the specified ID",
        tags = "Discounts",
        responses = {
                @ApiResponse(responseCode = "200", description = "Discount successfully updated"),
                @ApiResponse(responseCode = "404", description = "Discount not found"),
                @ApiResponse(responseCode = "400", description = "Invalid data")
        }
)
public @interface UpdateDiscount {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{id}"};
}
