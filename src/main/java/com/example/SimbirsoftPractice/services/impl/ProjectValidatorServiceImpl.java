package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.services.ProjectValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectValidatorServiceImpl implements ProjectValidatorService {

    private static final String WARN_CHANGE_STATUS = "Проект не может быть переведен из статуса %s в статус %s. Недопустимый переход";
    private static final String WARN_CLOSE_PROJECT = "Проект не может быть закрыт, так в нем есть не решенные задач";
    private static final String WARN_NULL_VALUE_FIELD = "Поле %s должно быть заполнено";
    private static final String INFO_START_CHECK_FIELD = "Начало проверки значения поля %s";
    private static final String INFO_GOOD_CHECKED_FIELD = "Поле %s успешно изменено";
    private static final String FIELD_NAME = "НАИМЕНОВАНИЕ";
    private static final String FIELD_DESCRIPTION = "ОПИСАНИЕ";
    private static final String FIELD_STATUS = "СТАТУС";
    private static final String FIELD_START_DATE = "ДАТА ОТКРЫТИЯ";
    private static final String FIELD_STOP_DATE = "ДАТА ЗАКРЫТИЯ";
    private static final String FIELD_CUSTOMER = "КЛИЕНТ";
    private static final StatusProject STATUS_DEFAULT = StatusProject.CREATED;
    private static final StatusProject STATUS_TO_SET_START_DATA = StatusProject.OPEN;
    private static final StatusProject STATUS_TO_SET_STOP_DATA = StatusProject.CLOSED;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TaskRepository taskRepository;

    public ProjectValidatorServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ProjectEntity validateInputValue(ProjectRequestDto newValue, ProjectEntity oldValue) {
        //other validation
        validateName(newValue, oldValue);
        validateDescription(newValue, oldValue);
        validateCustomer(newValue, oldValue);
        validateStatus(newValue, oldValue);
        validateStartDate(newValue, oldValue);
        validateStopDate(newValue, oldValue);
        return oldValue;
        //other validation
    }

    private void validateName(ProjectRequestDto newValue, ProjectEntity oldValue) {
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

    private void validateDescription(ProjectRequestDto newValue, ProjectEntity oldValue) {
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

    private void validateCustomer(ProjectRequestDto newValue, ProjectEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_CUSTOMER));
        CustomerEntity oValue = oldValue.getCustomer();
        Long nValue = newValue.getCustomer();
        //при создании записи поле должно быть заполнено
        if (nValue == null && oValue == null) {
            String text = String.format(WARN_NULL_VALUE_FIELD, FIELD_CUSTOMER);
            logger.warn(text);
            throw new NullValueFieldException(text);
        }
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(null или id не равны)
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            CustomerEntity entity = new CustomerEntity();
            entity.setId(nValue);
            oldValue.setCustomer(entity);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_CUSTOMER));
        }
    }

    private void validateStatus(ProjectRequestDto newValue, ProjectEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_STATUS));

        StatusProject oValue = oldValue.getStatus();
        StatusProject nValue = newValue.getStatus();
        //установка начального статуса, если он не было явно указан
        if (nValue == null && oValue == null) {
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
            oldValue.setStatus(STATUS_DEFAULT);
            return;
        }
        //проверка явно указанного статуса
        //если статус такой же, то завершаем проверку
        if (nValue != null && nValue != oValue) {
            //проверка на попытку перехода в статус с более низким рангом, что запрещено
            if (nValue.ordinal() < oValue.ordinal()) {
                String text = String.format(WARN_CHANGE_STATUS, oldValue.getStatus(), newValue.getStatus());
                logger.warn(text);
                throw new IllegalStatusException(text);
            }
            //дополнительная проверка, если статус требует этого
            if (nValue == StatusProject.CLOSED) {
                validateNotDoneTasks(oldValue.getId());
            } else if (nValue == StatusProject.OPEN) {
                validateNotDoneTasks(oldValue.getCustomer().getId());
            }
            oldValue.setStatus(nValue);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
        }
    }

    private void validateStartDate(ProjectRequestDto newValue, ProjectEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_START_DATE));
        Date oValue = oldValue.getStartDate();
        Date nValue = newValue.getStartDate();
        StatusProject status = oldValue.getStatus();
        //если дата не установлена явно и статус этого требует, то ставим текущую дату
        if (status.ordinal() >= STATUS_TO_SET_START_DATA.ordinal() && nValue == null && oValue == null) {
            oldValue.setStartDate(new Date());
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_START_DATE));
            return;
        }
        if (nValue != null) {
            //если дата установлена явно, то используем это значение
            oldValue.setStartDate(nValue);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_START_DATE));
        }
    }

    private void validateStopDate(ProjectRequestDto newValue, ProjectEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_STOP_DATE));
        Date oValue = oldValue.getStopDate();
        Date nValue = newValue.getStopDate();
        StatusProject status = oldValue.getStatus();
        //если дата установлена явно, то используем это значение
        if (nValue != null)  {
            oldValue.setStopDate(nValue);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STOP_DATE));
            return;
        }
        //если дата не установлена явно и статус этого требует, то ставим текущую дату
        if (status.ordinal() == STATUS_TO_SET_STOP_DATA.ordinal() && oValue == null) {
            oldValue.setStopDate(new Date());
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STOP_DATE));
        }
    }

    private void validateNotDoneTasks(Long projectId) {
        Long count = taskRepository.countTasksInProcessByProjectId(projectId, StatusTask.DONE);
        if (count != 0) {
            logger.warn(WARN_CLOSE_PROJECT);
            throw new IllegalStatusException(WARN_CLOSE_PROJECT);
        }
    }

    private void validateBankAccountCustomer(Long customerId) {

    }
}
