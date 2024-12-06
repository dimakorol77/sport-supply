package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Role;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAfterCreationDto {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private Role role;
    private LocalDateTime createdAt;
}
