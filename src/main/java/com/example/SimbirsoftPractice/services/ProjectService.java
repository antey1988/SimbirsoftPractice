package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto projectRequestDto);
    ProjectResponseDto readProject(Long id);
    ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id);
    void deleteProject(Long id);
    List<ProjectResponseDto> readListProjects();
    List<ProjectResponseDto> readListProjectsOfCustomer(Long id);
}
