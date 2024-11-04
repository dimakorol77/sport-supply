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
    //информация о пользователе после обновления
    private Long id; //подумать+телефон обновляли
    private String email;
    private String name;
    private LocalDateTime updatedAt;
}
