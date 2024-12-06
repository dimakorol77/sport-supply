package org.example.annotations.CartAnnotations;

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
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Emptying the cart",
        description = "Removes all items from the cart",
        tags = "Cart",
        responses = {
                @ApiResponse(responseCode = "204", description = "The cart has been successfully emptied"),
                @ApiResponse(responseCode = "404", description = "Cart not found"),
                @ApiResponse(responseCode = "403", description = "You don't have access")
        }
)
public @interface ClearCart {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{cartId}/clear"};
}
