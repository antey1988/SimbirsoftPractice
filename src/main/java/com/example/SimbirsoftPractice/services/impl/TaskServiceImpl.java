package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.mappers.TaskMapper;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.services.TaskService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    
    private final TaskMapper mapper;
    private final TaskRepository repository;

    public TaskServiceImpl(TaskMapper mapper, TaskRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        TaskEntity taskEntity = mapper.requestDtoToEntity(taskRequestDto, new TaskEntity());
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
        return TaskEntity.orElseThrow(() -> new NotFoundException(String.format("Задача с id = %d не существует", id)));
    }

    @Override
    public List<TaskResponseDto> readListTasksByReleaseId(Long id) {
        List<TaskEntity> list = repository.findAllByReleaseId(id);
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
    public List<TaskResponseDto> readListAllTasks() {
        List<TaskEntity> list = repository.findAll();
        return mapper.listEntityToListResponseDto(list);
    }
}
