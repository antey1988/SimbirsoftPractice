package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = TaskMapperImpl.class)
class TaskMapperImplTest {
    private static TaskEntity entity = new TaskEntity();
    private static TaskResponseDto response = new TaskResponseDto();
    @Autowired
    private TaskMapperImpl taskMapper;

    @BeforeAll
    static void setUp() {
        Long id = 1L;
        String name = "Task";
        String description = "Description";
        ReleaseEntity release = new ReleaseEntity();
        release.setId(1L);
        UserEntity creator = new UserEntity();
        release.setId(1L);
        UserEntity executor = new UserEntity();
        release.setId(1L);
        StatusTask status = StatusTask.BACKLOG;
        int border = 0;

        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setRelease(release);
        entity.setCreator(creator);
        entity.setExecutor(executor);
        entity.setStatus(status);
        entity.setBorder(border);

        response.setId(id);
        response.setName(name);
        response.setDescription(description);
        response.setRelease(release.getId());
        response.setCreator(creator.getId());
        response.setExecutor(executor.getId());
        response.setStatus(status);
        response.setBorder(border);
    }

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