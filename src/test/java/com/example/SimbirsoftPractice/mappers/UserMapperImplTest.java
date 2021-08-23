package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = UserMapperImpl.class)
class UserMapperImplTest {
    private static UserEntity entity;
    private static UserResponseDto response;
    @Autowired
    private UserMapperImpl userMapper;

    @BeforeAll
    static void setUp() {
        Long id = 1L;
        String name = "User";
        String password = "Password";
        Set<Role> roles = Set.of(Role.ROLE_CRUD_USERS, Role.ROLE_CRUD_OTHERS);

        entity = new UserEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setPassword(password);
        entity.setRoles(roles);

        response = new UserResponseDto();
        response.setId(id);
        response.setName(name);
        response.setRoles(roles);
    }

    @Test
    void entityToResponseDto() {
        UserResponseDto actual = response;
        UserResponseDto expected = userMapper.entityToResponseDto(entity);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertIterableEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void listEntityToListResponseDto() {
        List<UserResponseDto> actual = List.of(response, response);
        List<UserResponseDto> expected = userMapper.listEntityToListResponseDto(List.of(entity, entity));
        assertEquals(expected.size(), actual.size());
    }
}