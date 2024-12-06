package org.example.annotations.BrandAnnotations;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST)
@Operation(
        summary = "Creation of a new brand",
        description = "Creates a new brand",
        tags = "Brands",
        responses = {
                @ApiResponse(responseCode = "201", description = "The brand has been successfully created"),
                @ApiResponse(responseCode = "400", description = "Incorrect data")
        }
)
public @interface CreateBrand {
}

