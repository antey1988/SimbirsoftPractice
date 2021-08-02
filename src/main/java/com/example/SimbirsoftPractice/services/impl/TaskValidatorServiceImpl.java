package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStateStatusException;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.services.TaskValidatorService;
import org.springframework.stereotype.Service;

@Service
public class TaskValidatorServiceImpl implements TaskValidatorService {

    private static final String ERROR_CHANGE_STATUS = "Задача не может быть переведена из статуса %s в статус %s. Недопустимый переход";
    private static final String ERROR_BACKLOG_TASK = "Задача не может быть создана, так как проект уже закрыт";
    private static final String ERROR_IN_PROGRESS_TASK = "Задача не может быть взята в работу, так как проект еще не открыт";
    private static final String ERROR_DONE_TASK = "Задача не может быть выполена, так как проект еще не открыт";

    private final ProjectRepository repository;

    public TaskValidatorServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    public void validation(TaskRequestDto newValue, TaskEntity oldValue) {
        StatusProject statusProject = repository.getProjectByTask(oldValue.getId()).getStatus();
        switch (newValue.getStatus()) {
            //создание задачи
            case BACKLOG: {
                //если проект уже закрыт, то не допускается создавать новые задачи
                if (statusProject == StatusProject.CLOSED) {
                    throw new IllegalStateStatusException(ERROR_BACKLOG_TASK);
                }
                //задача не может быть переведена обратно в состояние созданной, если уже взята в работу или завершена
                if (oldValue.getStatus() == StatusTask.IN_PROGRESS ||
                        oldValue.getStatus() == StatusTask.DONE) {
                    throw new IllegalStateStatusException(String.format(ERROR_CHANGE_STATUS,
                            oldValue.getStatus(), newValue.getStatus()));
                }
                break;
            }
            //выполнение задачи
            case IN_PROGRESS: {
                //задачу нельзя взять в работу, если проект еще не открыт
                if (statusProject == StatusProject.CREATED) {
                    throw new IllegalStateStatusException(ERROR_IN_PROGRESS_TASK);
                }
                //задача не может быть взята в работу, если уже завершена
                if (oldValue.getStatus() == StatusTask.DONE) {
                    throw new IllegalStateStatusException(String.format(ERROR_CHANGE_STATUS,
                            oldValue.getStatus(), newValue.getStatus()));
                }
                break;
            }
            //окончание работы
            case DONE: {
                //задачу нельзя сразу завершить при создании, если проект еще не открыт
                if (statusProject == StatusProject.CREATED) {
                    throw new IllegalStateStatusException(ERROR_DONE_TASK);
                }
            }
        }
    }
}
