package org.example.annotations.OrderItemAnnotations;

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
        summary = "Deleting an order item",
        description = "Deletes the specified order item",
        tags = {"Order Items"},
        responses = {
                @ApiResponse(responseCode = "204", description = "Order item successfully deleted"),
                @ApiResponse(responseCode = "404", description = "Order item not found"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface DeleteOrderItem {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderItemId}"};
}
