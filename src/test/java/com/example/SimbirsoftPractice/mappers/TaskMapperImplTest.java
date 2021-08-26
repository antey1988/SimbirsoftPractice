package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.UtilTasks;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TaskMapperImplTest {
    private final TaskEntity entity = UtilTasks.defaultEntity();
    private final TaskResponseDto response = UtilTasks.defaultResponse();

    private TaskMapperImpl taskMapper = new TaskMapperImpl();

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