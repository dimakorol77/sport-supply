package org.example.annotations.CartItemAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.PUT)
@Operation(
        summary = "Updating the quantity of goods in the cart",
        description = "Updates the quantity of goods in the cart",
        tags = "Cart",
        responses = {
                @ApiResponse(responseCode = "200", description = "Product quantity updated"),
                @ApiResponse(responseCode = "404", description = "Product not found in cart"),
                @ApiResponse(responseCode = "403", description = "You don't have access")
        }
)
public @interface UpdateCartItemQuantity {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/items/{cartItemId}"};
}