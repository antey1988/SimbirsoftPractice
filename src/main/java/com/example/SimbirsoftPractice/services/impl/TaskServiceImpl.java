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
import com.example.SimbirsoftPractice.specificatons.TaskSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private static final String NOT_FOUND_TASK = "Задача с id = %d не существует";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        TaskEntity taskEntity = validator.validateInputValue(taskRequestDto, new TaskEntity());
        taskEntity = repository.save(taskEntity);
        logger.info("Новая запись добавлена в базу данных");
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
        taskEntity = validator.validateInputValue(taskRequestDto, taskEntity);
        logger.info("Запись обновлена в базе данных");
        return mapper.entityToResponseDto(taskEntity);
    }

    @Override
    public void deleteTask(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
        logger.info("Запись удалена из базы данных");
    }

    private TaskEntity getOrElseThrow(Long id) {
        logger.info(String.format("Попытка извлечения записи c id = %d из базы данных", id));
        Optional<TaskEntity> TaskEntity = repository.findById(id);
        TaskEntity entity = TaskEntity.orElseThrow(() -> {
            logger.warn(String.format("Запись c id = %d отсутсвует в базе данных", id));
            return new NotFoundException(String.format(NOT_FOUND_TASK, id));
        });
        logger.info(String.format("Запись c id = %d успешно извлечена из базы данных", id));
        return entity;
    }

    @Override
    public List<TaskResponseDto> readListTasksByReleaseId(Long id, List<StatusTask> statuses) {
//        if (statuses == null || statuses.isEmpty()){
//            statuses = Arrays.asList(StatusTask.BACKLOG, StatusTask.IN_PROGRESS);
//        }
//        List<TaskEntity> list = repository.findAllByReleaseId(id,  statuses);
        Specification<TaskEntity> specification = TaskSpecification.taskReleases(id).and(TaskSpecification.taskStatus(statuses));
        List<TaskEntity> list = repository.findAll(specification);
        logger.info(String.format("Список записей со значением поля release_id = %d и " +
                "status = %s извлечен из базы данных", id, statuses));
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListTasksByCreatorId(Long id) {
        List<TaskEntity> list = repository.findAllByCreatorId(id);
        logger.info(String.format("Список записей со значением поля creator_id = %d извлечен из базы данных", id));
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListTasksByExecutorId(Long id) {
        List<TaskEntity> list = repository.findAllByExecutorId(id);
        logger.info(String.format("Список записей со значением поля executor_id = %d извлечен из базы данных", id));
        return mapper.listEntityToListResponseDto(list);
    }

    @Override
    public List<TaskResponseDto> readListAllTasksByFilters(String name, String description, Long rId, Long cId, Long eId, List<StatusTask> statuses) {
        Specification<TaskEntity> specification = TaskSpecification.taskName(name)
                .and(TaskSpecification.taskDescription(description))
                .and(TaskSpecification.taskReleases(rId))
                .and(TaskSpecification.taskCreator(cId))
                .and(TaskSpecification.taskExecutor(eId))
                .and(TaskSpecification.taskStatus(statuses));
        List<TaskEntity> list =
        repository.findAll(specification);
//        repository.findAllByFilters(rId, cId, eId, statuses);
        logger.info(String.format("Список записей со значением поля name = %s, description = %s, release_id = %d, creator_id =  %d, " +
                "executor_id = %d и status = %s извлечен из базы данных", name, description, rId, cId, eId, statuses));
        return mapper.listEntityToListResponseDto(list);
    }
}
