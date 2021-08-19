package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;

import java.util.List;
import java.util.Locale;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectRequestDto projectRequestDto, Locale locale);
    ProjectResponseDto readProject(Long id, Locale locale);
    ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id, Locale locale);
    void deleteProject(Long id, Locale locale);
    List<ProjectResponseDto> readListProjects(Long id);
//    ProjectEntity readProjectByTaskId(Long id);
}
