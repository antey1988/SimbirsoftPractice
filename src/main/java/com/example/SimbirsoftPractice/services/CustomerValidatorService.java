package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;

public interface CustomerValidatorService {
    CustomerEntity validateOnCreation(CustomerRequestDto dto, CustomerEntity entity);
    CustomerEntity validateOnUpdate(CustomerRequestDto dto, CustomerEntity entity);
}
