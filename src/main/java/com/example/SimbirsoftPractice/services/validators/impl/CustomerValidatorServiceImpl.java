package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.services.validators.CustomerValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
public class CustomerValidatorServiceImpl implements CustomerValidatorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageSource messageSource;

    public CustomerValidatorServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public CustomerEntity validate(CustomerRequestDto dto, CustomerEntity entity, Locale locale) {
        //check field Name
        String nValue = dto.getName();
        String oValue = entity.getName();
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.name.alive", null, locale);
            logger.error("Attempting to save a customer with an empty field Name. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //update
        if (nValue != null && !nValue.equals(oValue)) {
            entity.setName(nValue);
            logger.info("Field Name changed successfully");
        }

        //check field UUID
        if (entity.getUuid() == null) {
            entity.setUuid(UUID.randomUUID());
            logger.info("Field Uuid changed successfully");
        }
        return entity;
    }
}
