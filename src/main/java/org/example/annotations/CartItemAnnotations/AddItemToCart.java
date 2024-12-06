package org.example.annotations.CartItemAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Adding an item to cart",
        description = "Adds a new product to the cart",
        tags = "Cart",
        responses = {
                @ApiResponse(responseCode = "201", description = "Product successfully added to cart"),
                @ApiResponse(responseCode = "400", description = "Invalid request data"),
                @ApiResponse(responseCode = "404", description = "Cart not found"),
                @ApiResponse(responseCode = "403", description = "You don't have access")
        }
)
public @interface AddItemToCart {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{cartId}/items"};
}