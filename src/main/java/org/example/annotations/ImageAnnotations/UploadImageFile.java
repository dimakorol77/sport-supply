package org.example.annotations.ImageAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
@Operation(summary = "Upload a file", description = "Uploads a file associated with a product")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
})
public @interface UploadImageFile {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/upload/file/{productId}"};

//    @Parameter(description = "File to upload", required = true, schema = @Schema(type = "string", format = "binary"))
//    String fileParam() default "file";
}
