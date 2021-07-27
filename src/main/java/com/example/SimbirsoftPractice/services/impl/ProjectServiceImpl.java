package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.mappers.ProjectMapper;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectMapper mapper;
    private final ProjectRepository repository;

    @Autowired
    public ProjectServiceImpl(ProjectMapper mapper, ProjectRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
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
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id) {
        ProjectEntity projectEntity = getOrElseThrow(id);
        projectEntity = mapper.requestDtoToEntity(projectRequestDto, projectEntity);
        projectEntity = repository.save(projectEntity);
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    public void deleteProject(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
    }

    private ProjectEntity getOrElseThrow(Long id) {
        Optional<ProjectEntity> projectEntity = repository.findById(id);
        return projectEntity.orElseThrow(()->new NotFoundException(String.format("Проект с id = %d не существует", id)));
    }
}
