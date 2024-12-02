package org.example.annotations.CartItemAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Removing an item from the cart",
        description = "Removes an item from the cart",
        tags = "Cart",
        responses = {
                @ApiResponse(responseCode = "204", description = "Product removed from cart"),
                @ApiResponse(responseCode = "404", description = "Product not found in cart"),
                @ApiResponse(responseCode = "403", description = "You don't have access")}
)
public @interface RemoveCartItem {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/items/{cartItemId}"};
}