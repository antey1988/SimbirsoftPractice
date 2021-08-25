package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.configurations.MessageSourceConfig;
import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MessageSourceConfig.class)
class CustomerValidatorServiceImplTest {
    private String name = "Name";
    private Locale locale = Locale.ENGLISH;

    private CustomerRequestDto actual = new CustomerRequestDto();

    @Autowired
    MessageSource messageSource;

    private CustomerValidatorServiceImpl validatorService;

    @Test
    void validateNotNull() {
        validatorService = new CustomerValidatorServiceImpl(messageSource);
        actual.setName(name);
        CustomerEntity expected = validatorService.validate(actual, new CustomerEntity(), locale);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test()
    void validateNull() {
        validatorService = new CustomerValidatorServiceImpl(messageSource);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(new CustomerRequestDto(), new CustomerEntity(), locale));
    }
}