package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.services.validators.ProjectValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

@Service
public class ProjectValidatorServiceImpl implements ProjectValidatorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TaskRepository taskRepository;
    private final MessageSource messageSource;

    public ProjectValidatorServiceImpl(TaskRepository taskRepository, MessageSource messageSource) {
        this.taskRepository = taskRepository;
        this.messageSource = messageSource;
    }

    @Override
    public ProjectEntity validate(ProjectRequestDto newValue, ProjectEntity oldValue, Locale locale) {
        //other validation
        validateName(newValue, oldValue, locale);
        validateDescription(newValue, oldValue);
        validateCustomer(newValue, oldValue, locale);
        validateStatus(newValue, oldValue, locale);
        validateStartDate(newValue, oldValue);
        validateStopDate(newValue, oldValue);
        validatePrice(newValue, oldValue);
        return oldValue;
        //other validation
    }

    private void validateName(ProjectRequestDto newValue, ProjectEntity oldValue, Locale locale) {
        String oValue = oldValue.getName();
        String nValue = newValue.getName();
        //при создании записи поле должно быть заполнено
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.name.notAline", null, locale);
            logger.error("Attempting to save a project with an empty field Name. Denied");
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

    private void validateDescription(ProjectRequestDto newValue, ProjectEntity oldValue) {
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

    private void validateCustomer(ProjectRequestDto newValue, ProjectEntity oldValue, Locale locale) {
        CustomerEntity oValue = oldValue.getCustomer();
        Long nValue = newValue.getCustomer();
        //при создании записи поле должно быть заполнено
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.customer", null, locale);
            logger.error("Attempting to save a project with an empty field Customer. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(null или id не равны)
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            CustomerEntity entity = new CustomerEntity();
            entity.setId(nValue);
            oldValue.setCustomer(entity);
            logger.info("Field Customer changed successfully");
        }
    }

    private void validateStatus(ProjectRequestDto newValue, ProjectEntity oldValue, Locale locale) {
        StatusProject oValue = oldValue.getStatus();
        StatusProject nValue = newValue.getStatus();
        //установка начального статуса, если он не было явно указан
        if (nValue == null && oValue == null) {
            oldValue.setStatus(StatusProject.CREATED);
            newValue.setStatus(StatusProject.CREATED);
            logger.info("Field Status changed successfully");
            return;
        }
        //проверка явно указанного статуса
        //если статус такой же, то завершаем проверку
        if (nValue != null && nValue != oValue) {
            //проверка на попытку перехода в статус с более низким рангом, что запрещено
            if (oValue != null && nValue.ordinal() < oValue.ordinal()) {
                String text = String.format("An attempt was made to save a project with an invalid status. " +
                                "Transition from status %s to status %s prohibited", oValue, nValue);
                logger.error(text);
                String error = messageSource.getMessage("error.IllegalStatus", null, locale);
                String record = messageSource.getMessage("record.Project", null, locale);
                throw new IllegalStatusException(String.format(error, record, oValue, nValue));
            }
            //дополнительная проверка, если статус требует этого
            if (nValue == StatusProject.CLOSED) {
                validateNotDoneTasks(oldValue.getId(), locale);
            }
            oldValue.setStatus(nValue);
            newValue.setStatus(nValue);
            logger.info("Field Status changed successfully");
        }
    }

    private void validateStartDate(ProjectRequestDto newValue, ProjectEntity oldValue) {
        StatusProject status = newValue.getStatus();
        //устанавливаем дату при открытии или закрытии проекта
        if (status == StatusProject.OPEN || status == StatusProject.CLOSED) {
            oldValue.setStartDate(new Date());
            logger.info("Field StartDate changed successfully");
        }
    }

    private void validateStopDate(ProjectRequestDto newValue, ProjectEntity oldValue) {
        //устанавливаем дату при закрытии проекта
        if (newValue.getStatus() == StatusProject.CLOSED) {
            oldValue.setStopDate(new Date());
            logger.info("Field StopDate changed successfully");
        }
    }

    private void validatePrice(ProjectRequestDto newValue, ProjectEntity oldValue) {
        BigDecimal oValue = oldValue.getPrice();
        BigDecimal nValue = newValue.getPrice();
        //при создании записи присваиваем нулевую стоимость
        //create
        if (nValue == null && oValue == null) {
            oldValue.setPrice(new BigDecimal("0.00"));
        }
        //при обновлении записи поле измениться на новое значение,
        //если оно отличается от старого
        //update
        if (nValue != null && !nValue.equals(oValue)) {
            oldValue.setPrice(nValue);
            logger.info("Field Price changed successfully");
        }
    }

    private void validateNotDoneTasks(Long projectId, Locale locale) {
        Long count = taskRepository.countTasksInProcessByProjectId(projectId, StatusTask.DONE);
        if (count != 0) {
            logger.warn("An attempt was made to save a project with unfinished tasks. Denied");
            throw new IllegalStatusException(
                    messageSource.getMessage("error.ProjectHaveNotDoneTasks", null, locale));
        }
    }
}
