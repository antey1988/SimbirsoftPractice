package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.UtilUsers;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperImplTest {
    private final UserEntity entity = UtilUsers.defaultEntity();
    private final UserResponseDto response= UtilUsers.defaultResponse();

    private UserMapperImpl userMapper = new UserMapperImpl();

    @Test
    void entityToResponseDto() {
        UserResponseDto actual = response;
        UserResponseDto expected = userMapper.entityToResponseDto(entity);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        Collection<Role> exp = expected.getRoles();
        Collection<Role> act = actual.getRoles();
        assertTrue(exp.size() == act.size() &&
                exp.containsAll(act) && act.containsAll(exp));
    }

    @Test
    void listEntityToListResponseDto() {
        List<UserResponseDto> actual = List.of(response, response);
        List<UserResponseDto> expected = userMapper.listEntityToListResponseDto(List.of(entity, entity));
        assertEquals(expected.size(), actual.size());
    }
}