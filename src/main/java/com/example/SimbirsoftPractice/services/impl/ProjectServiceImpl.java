package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.mappers.ProjectMapper;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.ProjectService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectMapper mapper;
    private final ProjectRepository repository;

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
    @Transactional
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id) {
        ProjectEntity projectEntity = getOrElseThrow(id);
        projectEntity =  mapper.requestDtoToEntity(projectRequestDto, projectEntity);
        return mapper.entityToResponseDto(projectEntity);
    }

    @Override
    public void deleteProject(Long id) {
        getOrElseThrow(id);
        repository.deleteById(id);
    }

    @Override
    public List<ProjectResponseDto> readListProjects(Long customerId) {
        List<ProjectEntity> list;
        if (customerId == null) {
            list = repository.findAll();
        } else {
            list = repository.findByCustomerId(customerId);
        }
        return mapper.listEntityToListResponseDto(list);
    }

    public List<ProjectResponseDto> readListProjectsOfCustomer(Long id) {
        List<ProjectEntity> list = repository.findByCustomerId(id);
        return mapper.listEntityToListResponseDto(list);
    }

    private ProjectEntity getOrElseThrow(Long id) {
        Optional<ProjectEntity> projectEntity = repository.findById(id);
        return projectEntity.orElseThrow(() -> new NotFoundException(String.format("Проект с id = %d не существует", id)));
    }
}
