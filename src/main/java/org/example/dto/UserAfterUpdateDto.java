package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAfterUpdateDto {

    private Long id;
    private String email;
    private String name;
    private LocalDateTime updatedAt;
}
