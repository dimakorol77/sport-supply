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
@RequestMapping(method = RequestMethod.DELETE)
@Operation(
        summary = "Cancel an order",
        description = "Cancels an order by its ID",
        tags = "Orders",
        responses = {
                @ApiResponse(responseCode = "204", description = "Order canceled"),
                @ApiResponse(responseCode = "400", description = "Order cannot be canceled"),
                @ApiResponse(responseCode = "404", description = "Order not found"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface CancelOrder {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{orderId}/cancel"};
}
