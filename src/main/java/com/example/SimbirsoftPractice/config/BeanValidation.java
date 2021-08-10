package com.example.SimbirsoftPractice.config;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.services.CustomerValidatorService;
import com.example.SimbirsoftPractice.services.impl.CustomerValidatorServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanValidation {

    @Bean
    public CustomerValidatorService customerValidatorService() throws NoSuchFieldException, NoSuchMethodException {
        return new CustomerValidatorServiceImpl(CustomerRequestDto.class, CustomerEntity.class);
    }
}
