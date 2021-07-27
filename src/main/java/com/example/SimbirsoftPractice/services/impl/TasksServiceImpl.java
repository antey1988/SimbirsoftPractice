package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.mappers.TaskMapper;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.services.TasksService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class TasksServiceImpl implements TasksService {
    
    private final TaskMapper mapper;
    private final TaskRepository repository;
    @Autowired
    public TasksServiceImpl(TaskMapper mapper, TaskRepository repository) {
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
    public TaskResponseDto updateTask(TaskRequestDto taskRequestDto, Long id) {
        TaskEntity taskEntity = getOrElseThrow(id);
        taskEntity = mapper.requestDtoToEntity(taskRequestDto, taskEntity);
        taskEntity = repository.save(taskEntity);
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    public void deleteTask(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
    }

    private TaskEntity getOrElseThrow(Long id) {
        Optional<TaskEntity> TaskEntity = repository.findById(id);
        return TaskEntity.orElseThrow(()->new NotFoundException(String.format("Проект с id = %d не существует", id)));
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
}
