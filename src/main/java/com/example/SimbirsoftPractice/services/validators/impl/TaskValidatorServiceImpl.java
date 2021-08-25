package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.entities.*;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.services.validators.TaskValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TaskValidatorServiceImpl implements TaskValidatorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProjectRepository repository;
    private final MessageSource messageSource;

    public TaskValidatorServiceImpl(ProjectRepository repository, MessageSource messageSource) {
        this.repository = repository;
        this.messageSource = messageSource;
    }

    public TaskEntity validate(TaskRequestDto newValue, TaskEntity oldValue, Locale locale) {
        //other validation
        validateName(newValue, oldValue, locale);
        validateDescription(newValue, oldValue);
        validateRelease(newValue, oldValue, locale);
        validateCreator(newValue, oldValue, locale);
        validateExecutor(newValue, oldValue, locale);
        validateStatus(newValue, oldValue, locale);
        oldValue.setBorder(newValue.getBorder());
        //other validation
        return oldValue;
    }

    private void validateName(TaskRequestDto newValue, TaskEntity oldValue, Locale locale) {
        String oValue = oldValue.getName();
        String nValue = newValue.getName();
        //при создании записи поле должно быть заполнено
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.name.notAline", null, locale);
            logger.error("Attempting to save a task with an empty field Name. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(в том числе null)
        //update
        if (nValue != null && !nValue.equals(oValue)) {
            oldValue.setName(nValue);
            logger.info("Field Name changed successfully");
        }
    }

    private void validateDescription(TaskRequestDto newValue, TaskEntity oldValue) {
        String oValue = oldValue.getDescription();
        String nValue = newValue.getDescription();
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(в том числе null)
        //update
        if (nValue != null && !nValue.equals(oValue)) {
            oldValue.setDescription(nValue);
            logger.info("Field Description changed successfully");
        }
    }

    private void validateCreator(TaskRequestDto newValue, TaskEntity oldValue, Locale locale) {
        UserEntity oValue = oldValue.getCreator();
        Long nValue = newValue.getCreator();
        //при создании записи поле должно быть заполнено
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.creator", null, locale);
            logger.error("Attempting to save a task with an empty field Creator. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(null или id не равны)
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            UserEntity entity = new UserEntity();
            entity.setId(nValue);
            oldValue.setCreator(entity);
            logger.info("Field Creator changed successfully");
        }
    }

    private void validateRelease(TaskRequestDto newValue, TaskEntity oldValue, Locale locale) {
        ReleaseEntity oValue = oldValue.getRelease();
        Long nValue = newValue.getRelease();
        //при создании записи поле должно быть заполнено
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.release", null, locale);
            logger.error("Attempting to save a task with an empty field Release. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(null или id не равны)
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            ReleaseEntity entity = new ReleaseEntity();
            entity.setId(nValue);
            oldValue.setRelease(entity);
            logger.info("Field Release changed successfully");
        }
    }

    private void validateStatus(TaskRequestDto newValue, TaskEntity oldValue, Locale locale) {
        StatusTask nValue = newValue.getStatus();
        StatusTask oValue = oldValue.getStatus();
        StatusProject status;
        //при создании задачи
        if (oValue == null) {
            status = repository.getProjectByRelease(newValue.getRelease()).getStatus();
            //если проект уже закрыт, то запрещено создавать задачи
            if (status == StatusProject.CLOSED) {
                logger.error("An attempt was made to create a task. Denied because the project is closed");
                throw new IllegalStatusException(
                        messageSource.getMessage("error.ProjectIsClosed", null, locale));
            }
            //установка начального статуса, если он не было явно указан
            nValue = nValue == null ? StatusTask.BACKLOG : nValue;
        }
        //при обновлении задачи
        else {
            //прекращаем проверку, если новое значение статуса не отличается от старого или null
            if (nValue == null || nValue == oValue) {
                return;
            }
            //переход на более ранний статус запрещен
            if (nValue.ordinal() < oValue.ordinal()) {
                String text = String.format("An attempt was made to save a task with an invalid status. " +
                        "Transition from status %s to status %s prohibited", oValue, nValue);
                logger.error(text);
                String error = messageSource.getMessage("error.IllegalStatus", null, locale);
                String record = messageSource.getMessage("record.Task", null, locale);
                throw new IllegalStatusException(String.format(error, record, oValue, nValue));
            }
            status = repository.getProjectByRelease(newValue.getRelease()).getStatus();
        }
        //если проект еще не открыт, то запрещено устанавливать статус IN_PROGRESS или DONE
        if (status == StatusProject.CREATED && (nValue == StatusTask.IN_PROGRESS || nValue == StatusTask.DONE)) {
            logger.error("An attempt was made to take to work or close a task. Refused because the project is not open yet");
            throw new IllegalStatusException(
                    messageSource.getMessage("error.ProjectIsNotOpen", null, locale));
        }
        oldValue.setStatus(nValue);
        newValue.setStatus(nValue);
        logger.info("Field Status changed successfully");
    }

    private void validateExecutor(TaskRequestDto newValue, TaskEntity oldValue, Locale locale) {
        UserEntity oValue = oldValue.getExecutor();
        Long nValue = newValue.getExecutor();
        StatusTask status = newValue.getStatus();
        if (nValue == null && oValue == null &&
                (status == StatusTask.IN_PROGRESS || status == StatusTask.DONE)) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.executor", null, locale);
            logger.error(String.format("Attempting to save a task in Status = %s with an empty field Executor. Denied", status));
            throw new NullValueFieldException(String.format(error, field));

        }
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            UserEntity entity = new UserEntity();
            entity.setId(newValue.getExecutor());
            oldValue.setExecutor(entity);
        }
        logger.info("Field Executor changed successfully");
    }
}
