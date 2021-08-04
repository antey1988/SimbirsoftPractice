package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.services.TaskValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskValidatorServiceImpl implements TaskValidatorService {

    private static final String WARN_CHANGE_STATUS = "Задача не может быть переведена из статуса %s в статус %s. Недопустимый переход";
    private static final String WARN_BACKLOG_TASK = "Задача не может быть создана, так как проект уже закрыт";
    private static final String WARN_IN_PROGRESS_TASK = "Задача не может быть взята в работу, так как проект еще не открыт";
    private static final String WARN_DONE_TASK = "Задача не может быть выполнена, так как проект еще не открыт";
    private static final String WARN_NULL_VALUE_FIELD = "Поле %s должно быть заполнено";
    private static final String INFO_START_CHECK_FIELD = "Начало проверки значения поля %s";
    private static final String INFO_GOOD_CHECKED_FIELD = "Поле %s успешно изменено";
    private static final String FIELD_NAME = "НАИМЕНОВАНИЕ";
    private static final String FIELD_DESCRIPTION = "ОПИСАНИЕ";
    private static final String FIELD_STATUS = "СТАТУС";
    private static final String FIELD_EXECUTOR = "ИСПОЛНИТЕЛЬ";
    private static final String FIELD_CREATOR = "СОЗДАТЕЛЬ";
    private static final String FIELD_RELEASE = "РЕЛИЗ";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProjectRepository repository;

    public TaskValidatorServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    public TaskEntity validation(TaskRequestDto newValue, TaskEntity oldValue) {
        //other validation
        validationNameAndDescription(newValue, oldValue);
        validationCreator(newValue, oldValue);
        validationRelease(newValue, oldValue);
        validationStatus(newValue, oldValue);
        validationExecutor(newValue, oldValue);
        oldValue.setBorder(newValue.getBorder());
        //other validation
        return oldValue;
    }

    private void validationNameAndDescription(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_NAME));
        if (newValue.getName() == null) {
            if (oldValue.getName() == null) {
                logger.warn("Наименование проекта не указано. Поле обязательно для заполнения");
                throw new NullValueFieldException(String.format(WARN_NULL_VALUE_FIELD, FIELD_NAME));
            }
        } else {
            if (oldValue.getName() == null || !oldValue.getName().equals(newValue.getName())) {
                oldValue.setName(newValue.getName());
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_NAME));

            }
        }

        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_DESCRIPTION));
        if (newValue.getDescription() != null) {
            oldValue.setDescription(newValue.getDescription());
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_DESCRIPTION));
        }
    }

    private void validationCreator(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_CREATOR));
        if (newValue.getCreator() == null) {
            if (oldValue.getCreator() == null) {
                logger.warn("Создатель задачи не указан");
                throw new NullValueFieldException(String.format(WARN_NULL_VALUE_FIELD, FIELD_CREATOR));
            }
        } else {
            if (oldValue.getCreator() == null || !oldValue.getCreator().getId().equals(newValue.getCreator())) {
                UserEntity entity = new UserEntity();
                entity.setId(newValue.getCreator());
                oldValue.setCreator(entity);
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_CREATOR));
            }
        }
    }

    private void validationRelease(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_RELEASE));
        if (newValue.getRelease() == null) {
            if (oldValue.getRelease() == null) {
                logger.warn("Релиз задачи не указан");
                throw new NullValueFieldException(String.format(WARN_NULL_VALUE_FIELD, FIELD_RELEASE));
            }
        } else {
            if (oldValue.getRelease() == null || !oldValue.getRelease().getId().equals(newValue.getRelease())) {
                ReleaseEntity entity = new ReleaseEntity();
                entity.setId(newValue.getRelease());
                oldValue.setRelease(entity);
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_RELEASE));
            }
        }
    }

    private void validationStatus(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_STATUS));
        //проверка статуса, если он не был явно указан
        if (newValue.getStatus() == null) {
            if (oldValue.getStatus() == null) {
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
                oldValue.setStatus(StatusTask.BACKLOG);
            }
            return;
        }
        //проверка явно указанного статуса
        //если статус такой же, то завершаем проверку
        if (newValue.getStatus() != oldValue.getStatus()) {
            logger.info("Извлечение информации о проекте, к которому прикреплена задача");
            StatusProject statusProject = repository.getProjectByTask(oldValue.getId()).getStatus();
            logger.info(String.format("Проект находиться в статусе %s", statusProject));
            logger.info(String.format("Попытка перевести задачу в статус %s", newValue.getStatus()));
            switch (newValue.getStatus()) {
                //создание задачи
                case BACKLOG: {
                    //если проект уже закрыт, то не допускается создавать новые задачи
                    if (statusProject == StatusProject.CLOSED) {
                        logger.warn("Не допустимый перевод. Проект уже закрыт");
                        throw new IllegalStatusException(WARN_BACKLOG_TASK);
                    }
                    //задача не может быть переведена обратно в состояние созданной, если уже взята в работу или завершена
                    if (oldValue.getStatus() == StatusTask.IN_PROGRESS ||
                            oldValue.getStatus() == StatusTask.DONE) {
                        logger.warn(String.format("Не допустимый перевод. Задача была в статусе %s", oldValue.getStatus()));
                        throw new IllegalStatusException(String.format(WARN_CHANGE_STATUS,
                                oldValue.getStatus(), newValue.getStatus()));
                    }
                    break;
                }
                //выполнение задачи
                case IN_PROGRESS: {
                    //задачу нельзя взять в работу, если проект еще не открыт
                    if (statusProject == StatusProject.CREATED) {
                        logger.warn("Не допустимый перевод. Проект еще не открыт");
                        throw new IllegalStatusException(WARN_IN_PROGRESS_TASK);
                    }
                    //задача не может быть взята в работу, если уже завершена
                    if (oldValue.getStatus() == StatusTask.DONE) {
                        logger.warn(String.format("Не допустимый перевод. Задача была в статусе %s", oldValue.getStatus()));
                        throw new IllegalStatusException(String.format(WARN_CHANGE_STATUS,
                                oldValue.getStatus(), newValue.getStatus()));
                    }
                    oldValue.setStatus(StatusTask.IN_PROGRESS);
                    break;
                }
                //окончание работы
                case DONE: {
                    //задачу нельзя сразу завершить при создании, если проект еще не открыт
                    if (statusProject == StatusProject.CREATED) {
                        logger.warn("Не допустимый перевод. Проект еще не открыт");
                        throw new IllegalStatusException(WARN_DONE_TASK);
                    }
                    oldValue.setStatus(StatusTask.DONE);
                }
            }
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
        }
    }

    private void validationExecutor(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_EXECUTOR));
        if (newValue.getExecutor() == null) {
            if (oldValue.getExecutor() == null && oldValue.getStatus() != StatusTask.BACKLOG) {
                logger.warn(String.format("Исполнитель задачи не указан. " +
                        "Для задачи в статусе %s поле исполнитель обязательно для заполнения", newValue.getStatus()));
                throw new NullValueFieldException(String.format(WARN_NULL_VALUE_FIELD, FIELD_EXECUTOR));
            }
        } else {
            if (oldValue.getExecutor() == null || !oldValue.getExecutor().getId().equals(newValue.getCreator())) {
                UserEntity entity = new UserEntity();
                entity.setId(newValue.getExecutor());
                oldValue.setExecutor(entity);
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_EXECUTOR));
            }
        }
    }
}
