package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;

public interface TaskValidatorService {

    void validation(TaskRequestDto newValue, TaskEntity oldValue);
}
