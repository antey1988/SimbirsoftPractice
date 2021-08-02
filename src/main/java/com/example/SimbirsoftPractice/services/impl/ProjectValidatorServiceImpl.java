package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStateStatusException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.services.ProjectValidatorService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectValidatorServiceImpl implements ProjectValidatorService {

    private static final String ERROR_CHANGE_STATUS = "Проект не может быть переведен из статуса %s в статус %s. Недопустимый переход";
    private static final String ERROR_CLOSE_PROJECT = "Проект не может быть закрыт, так в нем есть %d не решенных задач";

    private final TaskRepository taskRepository;

    public ProjectValidatorServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void validation(ProjectRequestDto newValue, ProjectEntity oldValue) {
        switch (newValue.getStatus()) {
            case CREATED: {
                if (oldValue.getStatus() == StatusProject.OPEN || oldValue.getStatus() == StatusProject.CLOSED) {
                    throw new IllegalStateStatusException(String.format(ERROR_CHANGE_STATUS,
                            oldValue.getStatus(), newValue.getStatus()));
                }
                break;
            }
            case OPEN: {
                if (oldValue.getStatus() == StatusProject.CLOSED) {
                    throw new IllegalStateStatusException(String.format(ERROR_CHANGE_STATUS,
                            oldValue.getStatus(), newValue.getStatus()));
                }
                if (newValue.getStartDate() == null) {
                    if (oldValue.getStartDate() == null) {
                        newValue.setStartDate(new Date());
                    } else {
                        newValue.setStartDate(oldValue.getStartDate());
                    }
                }
                break;
            }
            case CLOSED: {
                Long count = taskRepository.countTasksInProcessByProjectId(oldValue.getId(), StatusTask.DONE);
                if (count != 0) {
                    throw new IllegalStateStatusException(String.format(ERROR_CLOSE_PROJECT, count));
                }
                if (newValue.getStartDate() == null) {
                    if (oldValue.getStartDate() == null) {
                        newValue.setStartDate(new Date());
                    } else {
                        newValue.setStartDate(oldValue.getStartDate());
                    }
                }
                if (newValue.getStopDate() == null) {
                    if (oldValue.getStopDate() == null) {
                        newValue.setStopDate(new Date());
                    } else {
                        newValue.setStopDate(oldValue.getStopDate());
                    }
                }
            }
        }
    }
}
