package com.example.SimbirsoftPractice.configurations;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UtilUsers {
    private static final UtilUsers.UserBuilder defaultUser = builder()
            .id(1L).name("Name").password("Password").roles(Role.ROLE_CRUD_USERS, Role.ROLE_CRUD_OTHERS);

    public static UserResponseDto defaultResponse() {
        return defaultUser.buildResponse();
    }

    public static UserRequestDto defaultRequest() {
        return defaultUser.buildRequest();
    }

    public static UserEntity defaultEntity() {
        return defaultUser.buildEntity();
    }

    public static UtilUsers.UserBuilder builder() {
        return new UtilUsers.UserBuilder();
    }

    private static class UserBuilder {
        private Long id;
        private String name;
        private String password;
        private Set<Role> roles;

        public UtilUsers.UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UtilUsers.UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UtilUsers.UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UtilUsers.UserBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public UtilUsers.UserBuilder roles(Role ... roles) {
            this.roles = new HashSet<>(Arrays.asList(roles));
            return this;
        }

        public UserEntity buildEntity() {
            UserEntity value = new UserEntity();
            value.setId(this.id);
            value.setName(this.name);
            value.setPassword(this.password);
            value.setRoles(this.roles);
            return value;
        }

        public UserResponseDto buildResponse() {
            UserResponseDto value = new UserResponseDto();
            value.setId(this.id);
            value.setName(this.name);
            value.setRoles(this.roles);
            return value;
        }

        public UserRequestDto buildRequest() {
            UserRequestDto value = new UserRequestDto();
            value.setName(this.name);
            value.setPassword(this.password);
            value.setRoles(this.roles);
            return value;
        }
    }

}
