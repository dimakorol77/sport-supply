package org.example.annotations.PaymentAnnotations;

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
        summary = "Creating a new payment",
        description = "Creates a new payment for an order",
        tags = {"Payments"},
        responses = {
                @ApiResponse(responseCode = "201", description = "Payment successfully created"),
                @ApiResponse(responseCode = "404", description = "Order not found"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface CreatePayment {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/create"};
}
