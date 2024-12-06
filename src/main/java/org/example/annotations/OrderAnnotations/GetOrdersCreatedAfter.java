package org.example.annotations.OrderAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
        summary = "Getting orders after a specific date",
        description = "Returns a list of orders created after the specified date. Date format: YYYY-MM-DDTHH:MM:SS",
        tags = "Orders",
        parameters = {
                @Parameter(
                        name = "date",
                        description = "Date in ISO format. Example: 2023-10-01T00:00:00",
                        required = true,
                        in = ParameterIn.QUERY,
                        schema = @Schema(type = "string", format = "date-time", example = "2023-10-01T00:00:00")
                )
        },
        responses = {
                @ApiResponse(responseCode = "200", description = "Order list retrieved"),
                @ApiResponse(responseCode = "400", description = "Invalid date"),
                @ApiResponse(responseCode = "403", description = "Access denied")
        }
)

public @interface GetOrdersCreatedAfter {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/created-after"};
}
