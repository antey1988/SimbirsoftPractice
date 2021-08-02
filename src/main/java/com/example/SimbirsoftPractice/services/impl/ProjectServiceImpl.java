package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.mappers.ProjectMapper;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.ProjectService;
import com.example.SimbirsoftPractice.services.ProjectValidatorService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final String NOT_FOUND_PROJECT = "Проект с id = %d не существует";

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
        ProjectEntity projectEntity = mapper.requestDtoToEntity(projectRequestDto, new ProjectEntity());
        projectEntity = repository.save(projectEntity);
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
        validator.validation(projectRequestDto, projectEntity);
        projectEntity =  mapper.requestDtoToEntity(projectRequestDto, projectEntity);
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    public void deleteProject(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
    }

    @Override
    public List<ProjectResponseDto> readListProjects(Long id) {
        List<ProjectEntity> list;
        if (id == null) {
            list = repository.findAll();
        } else {
            list = repository.findByCustomerId(id);
        }
        return mapper.listEntityToListResponseDto(list);
    }

    public List<ProjectResponseDto> readListProjectsOfCustomer(Long id) {
        List<ProjectEntity> list = repository.findByCustomerId(id);
        return mapper.listEntityToListResponseDto(list);
    }

    private ProjectEntity getOrElseThrow(Long id) {
        Optional<ProjectEntity> projectEntity = repository.findById(id);
        return projectEntity.orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_PROJECT, id)));
    }

    @Override
    public ProjectEntity readProjectByTaskId(Long id) {
        return repository.getProjectByTask(id);
    }
}
