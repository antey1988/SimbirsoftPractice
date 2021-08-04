/**
 *
 *
 */
package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;

public interface ProjectValidatorService {

    ProjectEntity validation(ProjectRequestDto newValue, ProjectEntity oldValue);
}
