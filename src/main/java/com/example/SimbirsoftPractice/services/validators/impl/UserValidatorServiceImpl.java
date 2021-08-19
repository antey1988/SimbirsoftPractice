package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.services.validators.UserValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserValidatorServiceImpl implements UserValidatorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageSource messageSource;
    private final PasswordEncoder encoder;

    public UserValidatorServiceImpl(MessageSource messageSource, PasswordEncoder encoder) {
        this.messageSource = messageSource;
        this.encoder = encoder;
    }

    @Override
    public UserEntity validate(UserRequestDto dto, UserEntity entity, Locale locale) {
        validateName(dto, entity, locale);
        validatePassword(dto, entity, locale);
        return entity;
    }

    private void validateName(UserRequestDto dto, UserEntity entity, Locale locale) {
        String nValue = dto.getName();
        String oValue = entity.getName();
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.name.alive", null, locale);
            logger.error("Attempting to save a user with an empty field Name. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //update
        if (nValue != null && !nValue.equals(oValue)) {
            entity.setName(nValue);
            logger.info("Field Name changed successfully");
        }
    }

    private void validatePassword(UserRequestDto dto, UserEntity entity, Locale locale) {
        String nValue = dto.getPassword();
        String oValue = entity.getPassword();
        //create
        if (nValue == null && oValue == null) {
            String error = messageSource.getMessage("error.NullValueField", null, locale);
            String field = messageSource.getMessage("field.password", null, locale);
            logger.error("Attempting to save a user with an empty field Password. Denied");
            throw new NullValueFieldException(String.format(error, field));
        }
        //update
        if (nValue != null) {
            entity.setName(encoder.encode(nValue));
            logger.info("Field Password changed successfully");
        }
    }
}
