package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.utils.UtilCustomers;
import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CustomerValidatorServiceImplTest {
    private final Locale locale = Locale.ENGLISH;

    private final CustomerRequestDto expected = UtilCustomers.defaultRequest();
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CustomerValidatorServiceImpl validatorService;

    @Test
    void validateNotNull() {
        CustomerEntity actual = validatorService.validate(expected, new CustomerEntity(), locale);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test()
    void validateNullName() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(new CustomerRequestDto(), new CustomerEntity(), locale));
    }
}