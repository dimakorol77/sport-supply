package org.example.annotations.CartAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Converting a cart into an order",
        description = "Converts the cart into an order and updates the items in the cart",
        tags = "Cart",
        responses = {
                @ApiResponse(responseCode = "201", description = "Order created successfully"),
                @ApiResponse(responseCode = "400", description = "Cart is empty or request data is incorrect"),
                @ApiResponse(responseCode = "404", description = "Cart not found"),
                @ApiResponse(responseCode = "403", description = "You don't have access")
        }
)
public @interface ConvertCartToOrder {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/convert/{cartId}"};
}
