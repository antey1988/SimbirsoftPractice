package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.MappersEntityAndResponseDtoConfig;
import com.example.SimbirsoftPractice.configurations.MappersConfig;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MappersConfig.class, MappersEntityAndResponseDtoConfig.class})
class TaskMapperImplTest {
    @Autowired
    private TaskEntity entity;
    @Autowired
    private TaskResponseDto response;
    @Autowired
    private TaskMapperImpl taskMapper;

    @Test
    void entityToResponseDto() {
        TaskResponseDto actual = response;
        TaskResponseDto expected = taskMapper.entityToResponseDto(entity);
        assertEquals(expected, actual);
    }

    @Test
    void listEntityToListResponseDto() {
        List<TaskResponseDto> actual = List.of(response, response, response);
        List<TaskResponseDto> expected = taskMapper.listEntityToListResponseDto(List.of(entity, entity, entity));
        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }
}