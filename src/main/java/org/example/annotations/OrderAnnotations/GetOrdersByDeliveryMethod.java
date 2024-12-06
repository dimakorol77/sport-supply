package org.example.annotations.OrderAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.GET)
@Operation(
        summary = "Getting orders by delivery method",
        description = "Returns a list of orders with the specified delivery method",
        tags = "Orders",
        responses = {
                @ApiResponse(responseCode = "200", description = "Order list retrieved"),
                @ApiResponse(responseCode = "400", description = "Invalid delivery method"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)

public @interface GetOrdersByDeliveryMethod {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/delivery-method"};
}