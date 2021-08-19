package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.mappers.TaskMapper;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.services.TaskService;
import com.example.SimbirsoftPractice.services.validators.TaskValidatorService;
import com.example.SimbirsoftPractice.specificatons.TaskSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TaskMapper mapper;
    private final TaskRepository repository;
    private final TaskValidatorService validator;
    private final MessageSource messageSource;

    public TaskServiceImpl(TaskMapper mapper, TaskRepository repository,
                           TaskValidatorService validator, MessageSource messageSource) {
        this.mapper = mapper;
        this.repository = repository;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto, Locale locale) {
        TaskEntity taskEntity = validator.validate(taskRequestDto, new TaskEntity(), locale);
        taskEntity = repository.save(taskEntity);
        logger.info("New Task added to DB");
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    public TaskResponseDto readTask(Long id, Locale locale) {
        TaskEntity taskEntity = getOrElseThrow(id, locale);
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(TaskRequestDto taskRequestDto, Long id, Locale locale) {
        TaskEntity taskEntity = getOrElseThrow(id, locale);
        taskEntity = validator.validate(taskRequestDto, taskEntity, locale);
        logger.info("Task updated in the DB");
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    public void deleteTask(Long id, Locale locale) {
        getOrElseThrow(id, locale);
        repository.deleteById(id);
        logger.info("Task deleted from DB");
    }

    private TaskEntity getOrElseThrow(Long id, Locale locale ) {
        logger.info(String.format("Extracting Task with identifier(id) = %d from DB", id));
        Optional<TaskEntity> optional = repository.findById(id);
        return optional.orElseThrow(() -> {
            logger.error(String.format("Task with identifier (id) =% d does not exist", id));
            String error = messageSource.getMessage("error.NotFound", null, locale);
            String record = messageSource.getMessage("record.Task", null, locale);
            return new NotFoundException(String.format(error, record, id));
        });
    }

    @Override
    public List<TaskResponseDto> readListTasksByReleaseId(Long id, List<StatusTask> statuses) {
        Specification<TaskEntity> specification = TaskSpecification.createByTaskReleases(id).and(TaskSpecification.createByTaskStatus(statuses));
        List<TaskEntity> list = repository.findAll(specification);
        logger.info(String.format("List of tasks with field values(release_id = %d and status = %s retrieved from database", id, statuses));
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListTasksByCreatorId(Long id) {
        List<TaskEntity> list = repository.findAllByCreatorId(id);
        logger.info(String.format("List of tasks with field creator_id = %d retrieved from database", id));
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListTasksByExecutorId(Long id) {
        List<TaskEntity> list = repository.findAllByExecutorId(id);
        logger.info(String.format("List of tasks with field executor_id = %d retrieved from database", id));
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListAllTasksByFilters(String name, String description, Long rId, Long cId, Long eId, List<StatusTask> statuses) {
        Specification<TaskEntity> specification = TaskSpecification.createByTaskName(name)
                .and(TaskSpecification.createByTaskDescription(description))
                .and(TaskSpecification.createByTaskReleases(rId))
                .and(TaskSpecification.createByTaskCreator(cId))
                .and(TaskSpecification.createByTaskExecutor(eId))
                .and(TaskSpecification.createByTaskStatus(statuses));
        List<TaskEntity> list =
        repository.findAll(specification);
        logger.info(String.format("List of tasks with field values(name = %s, description = %s, release_id = %d, creator_id =  %d, " +
                "executor_id = %d and status = %s retrieved from database", name, description, rId, cId, eId, statuses));
        return mapper.listEntityToListResponseDto(list);
    }
}
