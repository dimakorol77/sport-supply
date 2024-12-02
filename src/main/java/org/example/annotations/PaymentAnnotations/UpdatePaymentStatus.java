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
@RequestMapping(method = RequestMethod.PATCH)
@Operation(
        summary = "Updating payment status",
        description = "Updates the status of an existing payment by its identifier",
        tags = {"Payments"},
        responses = {
                @ApiResponse(responseCode = "200", description = "Payment status successfully updated"),
                @ApiResponse(responseCode = "404", description = "Payment not found"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)
public @interface UpdatePaymentStatus {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/{paymentId}/status"};
}
