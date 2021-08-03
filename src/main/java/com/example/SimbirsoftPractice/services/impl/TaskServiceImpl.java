package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.mappers.TaskMapper;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.services.TaskService;
import com.example.SimbirsoftPractice.services.TaskValidatorService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private static final String NOT_FOUND_TASK = "Задача с id = %d не существует";

    private final TaskMapper mapper;
    private final TaskRepository repository;
    private final TaskValidatorService validator;

    public TaskServiceImpl(TaskMapper mapper, TaskRepository repository, TaskValidatorService validator) {
        this.mapper = mapper;
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        TaskEntity taskEntity = new TaskEntity();
        validator.validation(taskRequestDto, taskEntity);
        taskEntity = mapper.requestDtoToEntity(taskRequestDto, taskEntity);
        taskEntity = repository.save(taskEntity);
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    public TaskResponseDto readTask(Long id) {
        TaskEntity taskEntity = getOrElseThrow(id);
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(TaskRequestDto taskRequestDto, Long id) {
        TaskEntity taskEntity = getOrElseThrow(id);
        validator.validation(taskRequestDto, taskEntity);
        taskEntity = mapper.requestDtoToEntity(taskRequestDto, taskEntity);
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    public void deleteTask(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
    }

    private TaskEntity getOrElseThrow(Long id) {
        Optional<TaskEntity> TaskEntity = repository.findById(id);
        return TaskEntity.orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_TASK, id)));
    }

    @Override
    public List<TaskResponseDto> readListTasksByReleaseId(Long id, List<StatusTask> statuses) {
        if (statuses == null) {
            statuses = Arrays.asList(StatusTask.BACKLOG, StatusTask.IN_PROGRESS);
        }
        List<TaskEntity> list = repository.findAllByFilters(id, null, null, statuses);
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListTasksByCreatorId(Long id) {
        List<TaskEntity> list = repository.findAllByCreatorId(id);
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListTasksByExecutorId(Long id) {
        List<TaskEntity> list = repository.findAllByExecutorId(id);
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListAllTasksByFilters(Long rId, Long cId, Long eId, List<StatusTask> statuses) {
        List<TaskEntity> list = repository.findAllByFilters(rId, cId, eId, statuses);
        return mapper.listEntityToListResponseDto(list);
    }
}
