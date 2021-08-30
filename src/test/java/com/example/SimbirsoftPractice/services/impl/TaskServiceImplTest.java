package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.mappers.TaskMapper;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.services.validators.TaskValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    private final Long id_1 = 1L;
    private final Long id_2 = 2L;

    @Mock
    TaskRepository repository;
    @Mock
    TaskMapper mapper;
    @Mock
    TaskValidatorService validator;
    @Mock
    MessageSource messageSource;

    @InjectMocks
    TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(repository.findById(id_1)).thenReturn(Optional.of(new TaskEntity()));
        Mockito.lenient().when(mapper.entityToResponseDto(Mockito.any())).thenReturn(new TaskResponseDto());
    }

    @Test
    void readTask() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertNotNull(taskService.readTask(id_1, null));

        Mockito.when(repository.findById(id_2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.readTask(id_2, null));
    }

    @Test
    void createTask() {
        TaskEntity entity = new TaskEntity();

        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);

        assertNotNull(taskService.createTask(new TaskRequestDto(), null));
    }

    @Test
    void updateTask() {
        TaskEntity entity = new TaskEntity();

        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);

        assertNotNull(taskService.updateTask(new TaskRequestDto(), id_1, null));
    }

    @Test
    void deleteTask() {
        Mockito.doNothing().when(repository).deleteById(id_1);

        taskService.deleteTask(id_1, null);
        Mockito.verify(repository).deleteById(id_1);
    }

    @Test
    void readListAllTasksByFilters() {
        List<TaskEntity> list = List.of(new TaskEntity(), new TaskEntity());

        Mockito.when(repository.findAll(Mockito.any(Specification.class))).thenReturn(list);
        Mockito.when(mapper.listEntityToListResponseDto(list))
                .thenReturn(List.of(new TaskResponseDto(), new TaskResponseDto()));

        List<TaskResponseDto> response = taskService
                .readListAllTasksByFilters(null, null, null, null, null, null);
        assertNotNull(response);
        assertEquals(response.size(), 2);
    }

    @Test
    void readListTasksByCreatorId() {
        List<TaskEntity> list = List.of(new TaskEntity(), new TaskEntity());

        Mockito.when(repository.findAllByCreatorId(id_1)).thenReturn(list);
        Mockito.when(mapper.listEntityToListResponseDto(list))
                .thenReturn(List.of(new TaskResponseDto(), new TaskResponseDto()));

        List<TaskResponseDto> actual = taskService.readListTasksByCreatorId(id_1);
        assertNotNull(actual);
        assertEquals(actual.size(), 2);
    }

    @Test
    void readListTasksByExecutorId() {
        List<TaskEntity> list = List.of(new TaskEntity(), new TaskEntity());

        Mockito.when(repository.findAllByExecutorId(id_1)).thenReturn(list);
        Mockito.when(mapper.listEntityToListResponseDto(list))
                .thenReturn(List.of(new TaskResponseDto(), new TaskResponseDto()));

        List<TaskResponseDto> response = taskService.readListTasksByExecutorId(id_1);
        assertNotNull(response);
        assertEquals(response.size(), 2);
    }
}