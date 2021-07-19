package com.example.SimbirsoftPractice.rest.dto;

import com.example.SimbirsoftPractice.rest.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Данные о пользователе (from front to back)")
public class UserRequestDto {
    @Schema(description = "Имя пользователя")
    private String name;
    @Schema(description = "Роли пользователя")
    private List<Role> roles;

    /*public UserRequestDto(String name, List<Role> roles) {
        this.name = name;
        this.roles = roles;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
