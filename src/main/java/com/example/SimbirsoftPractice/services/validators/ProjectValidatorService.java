/**
 *
 *
 */
package com.example.SimbirsoftPractice.services.validators;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;

import java.util.Locale;

public interface ProjectValidatorService {

    ProjectEntity validate(ProjectRequestDto newValue, ProjectEntity oldValue, Locale locale);
}
