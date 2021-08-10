package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.*;
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
    private static final String WARN_PROJECT_IS_CLOSED = "Задача не может быть создана, так как проект уже закрыт";
    private static final String WARN_PROJECT_IS_NOT_OPEN = "Задача не может быть переведена в статус %s, так как проект еще не открыт";
    private static final String WARN_NULL_VALUE_FIELD = "Поле %s должно быть заполнено";
    private static final String INFO_START_CHECK_FIELD = "Начало проверки значения поля %s";
    private static final String INFO_GOOD_CHECKED_FIELD = "Поле %s успешно изменено";
    private static final String FIELD_NAME = "НАИМЕНОВАНИЕ";
    private static final String FIELD_DESCRIPTION = "ОПИСАНИЕ";
    private static final String FIELD_STATUS = "СТАТУС";
    private static final String FIELD_EXECUTOR = "ИСПОЛНИТЕЛЬ";
    private static final String FIELD_CREATOR = "СОЗДАТЕЛЬ";
    private static final String FIELD_RELEASE = "РЕЛИЗ";
    private static final StatusTask STATUS_TO_SET_EXECUTOR = StatusTask.IN_PROGRESS;
    private static final StatusTask STATUS_DEFAULT = StatusTask.BACKLOG;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProjectRepository repository;

    public TaskValidatorServiceImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    public TaskEntity validateInputValue(TaskRequestDto newValue, TaskEntity oldValue) {
        //other validation
        validateName(newValue, oldValue);
        validateDescription(newValue, oldValue);
        validateCreator(newValue, oldValue);
        validateRelease(newValue, oldValue);
        validateStatus(newValue, oldValue);
        validateExecutor(newValue, oldValue);
        oldValue.setBorder(newValue.getBorder());
        //other validation
        return oldValue;
    }


    private void validateName(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_NAME));
        String oValue = oldValue.getName();
        String nValue = newValue.getName();
        //при создании записи поле должно быть заполнено
        if (nValue == null && oValue == null) {
            String text = String.format(WARN_NULL_VALUE_FIELD, FIELD_NAME);
            logger.warn(text);
            throw new NullValueFieldException(text);
        }
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(в том числе null)
        if (nValue != null && !nValue.equals(oValue)) {
            oldValue.setName(nValue);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_NAME));
        }
    }

    private void validateDescription(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_DESCRIPTION));
        String oValue = oldValue.getDescription();
        String nValue = newValue.getDescription();
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(в том числе null)
        if (nValue != null && !nValue.equals(oValue)) {
            oldValue.setDescription(nValue);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_DESCRIPTION));
        }
    }

    private void validateCreator(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_CREATOR));
        UserEntity oValue = oldValue.getCreator();
        Long nValue = newValue.getCreator();
        if (nValue == null && oValue == null) {
            String text = String.format(WARN_NULL_VALUE_FIELD, FIELD_CREATOR);
            logger.warn(text);
            throw new NullValueFieldException(text);
        }
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            UserEntity entity = new UserEntity();
            entity.setId(nValue);
            oldValue.setCreator(entity);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_CREATOR));
        }
    }

    private void validateRelease(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_RELEASE));
        ReleaseEntity oValue = oldValue.getRelease();
        Long nValue = newValue.getRelease();
        if (nValue == null && oValue == null) {
            logger.warn("Релиз задачи не указан");
            throw new NullValueFieldException(String.format(WARN_NULL_VALUE_FIELD, FIELD_RELEASE));

        }
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            ReleaseEntity entity = new ReleaseEntity();
            entity.setId(nValue);
            oldValue.setRelease(entity);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_RELEASE));
        }
    }

    private void validateStatus(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_STATUS));
        StatusTask nValue = newValue.getStatus();
        StatusTask oValue = oldValue.getStatus();
        StatusProject statusProject;
        //при создании задачи
        if (oValue == null) {
            statusProject = repository.getProjectByRelease(newValue.getRelease()).getStatus();
            //если проект уже закрыт, то запрещено создавать задачи
            if (statusProject == StatusProject.CLOSED) {
                logger.warn(WARN_PROJECT_IS_CLOSED);
                throw new IllegalStatusException(WARN_PROJECT_IS_CLOSED);
            }
            //установка начального статуса, если он не было явно указан
            nValue = nValue == null ? STATUS_DEFAULT : nValue;
        }
        //при обновлении задачи
        else {
            //прекращаем проверку, если новое значение статуса не отличается от старого или null
            if (nValue == null || nValue == oValue) {
                return;
            }
            //переход на более ранний статус запрещен
            if (nValue.ordinal() < oValue.ordinal()) {
                String text = String.format(WARN_CHANGE_STATUS, oValue, nValue);
                logger.warn(text);
                throw new IllegalStatusException(text);
            }
            statusProject = repository.getProjectByRelease(newValue.getRelease()).getStatus();
        }
        //если проект еще не открыт, то запрещено устанавливать статус IN_PROGRESS или DONE
        if (statusProject == StatusProject.CREATED && (nValue == StatusTask.IN_PROGRESS || nValue == StatusTask.DONE)) {
            String text = String.format(WARN_PROJECT_IS_NOT_OPEN, nValue);
            logger.warn(text);
            throw new IllegalStatusException(text);
        }
        oldValue.setStatus(nValue);
        logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
    }

    private void validateExecutor(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_EXECUTOR));
        UserEntity oValue = oldValue.getExecutor();
        Long nValue = newValue.getExecutor();
        StatusTask status = newValue.getStatus();
        if (nValue == null && oValue == null && status.ordinal() >= STATUS_TO_SET_EXECUTOR.ordinal()) {
            String text = String.format(WARN_NULL_VALUE_FIELD, FIELD_EXECUTOR);
            logger.warn(text);
            throw new NullValueFieldException(text);

        }
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            UserEntity entity = new UserEntity();
            entity.setId(newValue.getExecutor());
            oldValue.setExecutor(entity);
        }
        logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_EXECUTOR));
    }
}
