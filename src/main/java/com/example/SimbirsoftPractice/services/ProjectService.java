package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto projectRequestDto);
    ProjectResponseDto getProject(Long id);
    ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id);
    void deleteProject(Long id);
}
