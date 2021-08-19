package com.example.SimbirsoftPractice.services.validators;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;

import java.util.Locale;

public interface TaskValidatorService {
    TaskEntity validate(TaskRequestDto newValue, TaskEntity oldValue, Locale locale);
}
