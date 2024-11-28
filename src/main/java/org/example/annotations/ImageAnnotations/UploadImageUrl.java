package org.example.annotations.ImageAnnotations;

import io.swagger.v3.oas.annotations.Operation;
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
@Operation(summary = "Upload an image URL", description = "Uploads an image from a URL associated with a product")
public @interface UploadImageUrl {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {"/upload/url/{productId}"};
}
