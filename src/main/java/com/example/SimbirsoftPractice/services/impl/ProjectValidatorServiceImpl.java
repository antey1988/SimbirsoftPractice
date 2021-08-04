package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.services.ProjectValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectValidatorServiceImpl implements ProjectValidatorService {

    private static final String WARN_CHANGE_STATUS = "Проект не может быть переведен из статуса %s в статус %s. Недопустимый переход";
    private static final String WARN_CLOSE_PROJECT = "Проект не может быть закрыт, так в нем есть %d не решенных задач";
    private static final String WARN_NULL_VALUE_FIELD = "Поле %s должно быть заполнено";
    private static final String INFO_START_CHECK_FIELD = "Начало проверки значения поля %s";
    private static final String INFO_GOOD_CHECKED_FIELD = "Поле %s успешно изменено";
    private static final String FIELD_NAME = "НАИМЕНОВАНИЕ";
    private static final String FIELD_DESCRIPTION = "ОПИСАНИЕ";
    private static final String FIELD_STATUS = "СТАТУС";
    private static final String FIELD_START_DATE = "ДАТА ОТКРЫТИЯ";
    private static final String FIELD_STOP_DATE = "ДАТА ЗАКРЫТИЯ";
    private static final String FIELD_CUSTOMER = "КЛИЕНТ";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TaskRepository taskRepository;

    public ProjectValidatorServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public ProjectEntity validation(ProjectRequestDto newValue, ProjectEntity oldValue) {
        //other validation
        validationNameAndDescription(newValue, oldValue);
        validationCustomer(newValue, oldValue);
        validationStatus(newValue, oldValue);
        validationStartAndStopDate(newValue, oldValue);
        return oldValue;
        //other validation
    }

    private void validationNameAndDescription(ProjectRequestDto newValue, ProjectEntity oldValue) {
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

    private void validationCustomer(ProjectRequestDto newValue, ProjectEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_CUSTOMER));
        if (newValue.getCustomer() == null) {
            if (oldValue.getCustomer() == null) {
                logger.warn("Клиент не указан");
                throw new NullValueFieldException(String.format(WARN_NULL_VALUE_FIELD, FIELD_CUSTOMER));
            }
        } else {
            if (oldValue.getCustomer() == null || !oldValue.getCustomer().getId().equals(newValue.getCustomer())) {
                CustomerEntity entity = new CustomerEntity();
                entity.setId(newValue.getCustomer());
                oldValue.setCustomer(entity);
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_CUSTOMER));
            }
        }
    }

    private void validationStatus(ProjectRequestDto newValue, ProjectEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_STATUS));
        //проверка статуса, если он не было явно указан
        if (newValue.getStatus() == null) {
            if (oldValue.getStatus() == null) {
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
                oldValue.setStatus(StatusProject.CREATED);
            }
            return;
        }
        //проверка явно указанного статуса
        //если статус такой же, то завершаем проверку
        if (newValue.getStatus() != oldValue.getStatus()) {
            switch (newValue.getStatus()) {
                case CREATED: {
                    if (oldValue.getStatus() == StatusProject.OPEN || oldValue.getStatus() == StatusProject.CLOSED) {
                        logger.warn(String.format("Не допустимый перевод. Проект была в статусе %s", oldValue.getStatus()));
                        throw new IllegalStatusException(String.format(WARN_CHANGE_STATUS,
                                oldValue.getStatus(), newValue.getStatus()));
                    }
                    break;
                }
                case OPEN: {
                    if (oldValue.getStatus() == StatusProject.CLOSED) {
                        logger.warn(String.format("Не допустимый перевод. Проект была в статусе %s", oldValue.getStatus()));
                        throw new IllegalStatusException(String.format(WARN_CHANGE_STATUS,
                                oldValue.getStatus(), newValue.getStatus()));
                    }
                    oldValue.setStatus(StatusProject.OPEN);
                    break;
                }
                case CLOSED: {
                    Long count = taskRepository.countTasksInProcessByProjectId(oldValue.getId(), StatusTask.DONE);
                    if (count != 0) {
                        logger.warn("Отказано в переводе статуса в значение ЗАКРЫТ, так как в проекте есть не закрытые задачи");
                        throw new IllegalStatusException(String.format(WARN_CLOSE_PROJECT, count));
                    }
                    oldValue.setStatus(StatusProject.CLOSED);
                }
            }
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
        }
    }

    private void validationStartAndStopDate(ProjectRequestDto newValue, ProjectEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_START_DATE));
        //если дата не установлена явно, то
        if (newValue.getStartDate() == null) {
            //проверка на ранее установленное значение и статус
            if (oldValue.getStartDate() == null && oldValue.getStatus() != StatusProject.CREATED) {
                oldValue.setStartDate(new Date());
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_START_DATE));
            }
        } else {
            //если дата установлена явно, то используем это значение
            oldValue.setStartDate(newValue.getStartDate());
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_START_DATE));
        }

        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_STOP_DATE));
        if (newValue.getStopDate() == null) {
            //проверка на ранее установленное значение и статус
            if (oldValue.getStopDate() == null && oldValue.getStatus() == StatusProject.CLOSED) {
                oldValue.setStopDate(new Date());
                logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STOP_DATE));
            }
        } else {
            //если дата установлена явно, то используем это значение
            oldValue.setStopDate(newValue.getStopDate());
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STOP_DATE));
        }
    }
}
