package org.example.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ImageDto {
    private Long id;

    @NotNull(message = "URL cannot be null")
    @Size(min = 1, message = "URL cannot be empty")
    private String url;

    @Size(max = 255, message = "Alt text cannot exceed 255 characters")
    private String altText;

    private Long productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
