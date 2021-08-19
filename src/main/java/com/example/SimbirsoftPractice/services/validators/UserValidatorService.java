package com.example.SimbirsoftPractice.services.validators;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;

import java.util.Locale;

public interface UserValidatorService {
    UserEntity validate(UserRequestDto dto, UserEntity entity, Locale locale);
}
