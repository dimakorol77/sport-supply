package org.example.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    //данные, которые могут быть обновлены для существующего пользователя
    private String email;
    private String name;
    private String phoneNumber;
    private String password; //подумать надо ли
}
