package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.services.validators.ReleaseValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service
public class ReleaseValidatorServiceImpl implements ReleaseValidatorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageSource messageSource;

    public ReleaseValidatorServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public ReleaseEntity validate(ReleaseRequestDto dto, ReleaseEntity entity, Locale locale) {
        validateName(dto, entity, locale);
        validateProject(dto, entity, locale);
        validateDates(dto, entity);
        return entity;
    }

    private void validateName(ReleaseRequestDto newValue, ReleaseEntity oldValue, Locale locale) {
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

    private void validateProject(ReleaseRequestDto newValue, ReleaseEntity oldValue, Locale locale) {
        ProjectEntity oValue = oldValue.getProject();
        Long nValue = newValue.getProject();
        //при создании записи поле должно быть заполнено
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.project", null, locale);
            logger.error("Attempting to save a release with an empty field Project. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(null или id не равны)
        if (nValue != null && (oValue == null || !nValue.equals(oValue.getId()))) {
            ProjectEntity entity = new ProjectEntity();
            entity.setId(nValue);
            oldValue.setProject(entity);
            logger.info("Field Project changed successfully");
        }
    }

    private void validateDates(ReleaseRequestDto newValue, ReleaseEntity oldValue) {
        //StartDate
        Date oValue = oldValue.getStartDate();
        Date nValue = newValue.getStartDate();
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(в том числе null)
        //update
        if (nValue != null && !nValue.equals(oValue)) {
            oldValue.setStartDate(nValue);
            logger.info("Field StartDate changed successfully");
        }
        //StopDate
        oValue = oldValue.getStopDate();
        nValue = newValue.getStopDate();
        //при создании и обновлении записи поле измениться на новое значение,
        //если оно отличается от старого(в том числе null)
        //update
        if (nValue != null && !nValue.equals(oValue)) {
            oldValue.setStopDate(nValue);
            logger.info("Field StopDate changed successfully");
        }
    }
}
