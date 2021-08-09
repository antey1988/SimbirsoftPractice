package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.services.CustomerValidatorService;
import com.example.SimbirsoftPractice.validators.BaseValidator;

public class CustomerValidatorServiceImpl extends BaseValidator<CustomerRequestDto, CustomerEntity> implements CustomerValidatorService {

    public CustomerValidatorServiceImpl(Class<CustomerRequestDto> dto, Class<CustomerEntity> entity) throws NoSuchMethodException, NoSuchFieldException {
        super(dto, entity);
    }
}
