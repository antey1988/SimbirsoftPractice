package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.services.CustomerValidatorService;
import com.example.SimbirsoftPractice.validators.BaseValidator;
import com.example.SimbirsoftPractice.validators.Validing;

import java.util.UUID;

public class CustomerValidatorServiceImpl extends BaseValidator<CustomerRequestDto, CustomerEntity> implements CustomerValidatorService {

    public CustomerValidatorServiceImpl(Class<CustomerRequestDto> dto, Class<CustomerEntity> entity) throws NoSuchMethodException, NoSuchFieldException {
        super(dto, entity);
    }

    @Validing(field = "uuid")
    public void validateUuid(CustomerRequestDto dto,  CustomerEntity entity) {
        if (entity.getUuid() == null) {
            entity.setUuid(UUID.randomUUID());
        }
    }
}
