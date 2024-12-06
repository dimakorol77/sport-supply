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
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Creating an order item",
        description = "Creates a new item for the specified order",
        tags = {"Order Items"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Order item successfully created"),
                @ApiResponse(responseCode = "400", description = "Request data error"),
                @ApiResponse(responseCode = "404", description = "Order not found"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface CreateOrderItem {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderId}"};
}
