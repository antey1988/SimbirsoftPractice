package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.mappers.ProjectMapper;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.ProjectService;
import com.example.SimbirsoftPractice.services.ProjectValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final String NOT_FOUND_PROJECT = "Проект с id = %d не существует";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProjectMapper mapper;
    private final ProjectRepository repository;
    private final ProjectValidatorService validator;

    public ProjectServiceImpl(ProjectMapper mapper, ProjectRepository repository, ProjectValidatorService validator) {
        this.mapper = mapper;
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto) {
        ProjectEntity projectEntity = validator.validateInputValue(projectRequestDto, new ProjectEntity());
        projectEntity = repository.save(projectEntity);
        logger.info("Новая запись добавлена в базу данных");
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    public ProjectResponseDto readProject(Long id) {
        ProjectEntity projectEntity = getOrElseThrow(id);
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id) {
        ProjectEntity projectEntity = getOrElseThrow(id);
        projectEntity =  validator.validateInputValue(projectRequestDto, projectEntity);
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    public void deleteProject(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
        logger.info("Запись удалена из базы данных");
    }

    @Override
    public List<ProjectResponseDto> readListProjects(Long id) {
        List<ProjectEntity> list;
        if (id == null) {
            list = repository.findAll();
            logger.info("Список записей извлечен из базы данных");
        } else {
            list = repository.findByCustomerId(id);
            logger.info(String.format("Список записей со значение поля custom_id = %d извлечен из базы данных", id));
        }
        return mapper.listEntityToListResponseDto(list);
    }

    private ProjectEntity getOrElseThrow(Long id) {
        logger.info(String.format("Попытка извлечения записи c id = %d из базы данных", id));
        Optional<ProjectEntity> projectEntity = repository.findById(id);
        ProjectEntity entity = projectEntity.orElseThrow(() -> {
            logger.warn(String.format("Запись c id = %d отсутсвует в базе данных", id));
            return new NotFoundException(String.format(NOT_FOUND_PROJECT, id));
        });
        logger.info(String.format("Запись c id = %d успешно извлечена из базы данных", id));
        return entity;
    }

//    @Override
//    public ProjectEntity readProjectByTaskId(Long id) {
//        return repository.getProjectByTask(id);
//    }
}
