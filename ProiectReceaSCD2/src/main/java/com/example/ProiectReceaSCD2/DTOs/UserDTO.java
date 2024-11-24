package com.example.ProiectReceaSCD2.DTOs;

import com.example.ProiectReceaSCD2.Entities.UserEntity;
import lombok.Getter;
import lombok.Setter;

public class UserDTO {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String email;

    public UserDTO(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }


}