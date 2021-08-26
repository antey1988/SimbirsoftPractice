package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.MappersEntityAndResponseDtoConfig;
import com.example.SimbirsoftPractice.configurations.MappersConfig;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MappersConfig.class, MappersEntityAndResponseDtoConfig.class})
class UserMapperImplTest {
    @Autowired
    private UserEntity entity;
    @Autowired
    private UserResponseDto response;
    @Autowired
    private UserMapperImpl userMapper;

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