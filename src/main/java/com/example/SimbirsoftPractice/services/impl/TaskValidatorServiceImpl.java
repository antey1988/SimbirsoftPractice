package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.*;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.Verificable;
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

        Verificable oValue = oldValue.getStatus();
        Verificable nValue = newValue.getStatus();
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
            if (nValue.getRank() < oValue.getRank()) {
                String text = String.format(WARN_CHANGE_STATUS, oldValue.getStatus(), newValue.getStatus());
                logger.warn(text);
                throw new IllegalStatusException(text);
            }
            //дополнительная проверка, если статус требует этого
            if (nValue.isVerificable()) {
                validateStatusAdditionally(nValue.getNumberVerification(), oldValue.getId(), StatusTask.DONE);
            }
            oldValue.setStatus(nValue.getEnum());

            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_STATUS));
        }


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

    private void validateExecutor(TaskRequestDto newValue, TaskEntity oldValue) {
        logger.debug(String.format(INFO_START_CHECK_FIELD, FIELD_EXECUTOR));
        UserEntity oValue = oldValue.getExecutor();
        Long nValue = newValue.getExecutor();
        Verificable status = oldValue.getStatus();
        if (nValue == null && oValue == null && status.getRank() >= STATUS_TO_SET_EXECUTOR.getRank()) {
            String text = String.format(WARN_NULL_VALUE_FIELD, FIELD_EXECUTOR);
            logger.warn(text);
            throw new NullValueFieldException(text);

        }
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            UserEntity entity = new UserEntity();
            entity.setId(newValue.getExecutor());
            oldValue.setExecutor(entity);
            logger.info(String.format(INFO_GOOD_CHECKED_FIELD, FIELD_EXECUTOR));
        }
    }
}
