package com.example.SimbirsoftPractice.services.validators;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;

import java.util.Locale;

public interface CustomerValidatorService {
    CustomerEntity validate(CustomerRequestDto dto, CustomerEntity entity, Locale locale);
}
