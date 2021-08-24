package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.configurations.MessageSourceConfig;
import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MessageSourceConfig.class)
@ExtendWith(MockitoExtension.class)
class ReleaseValidatorServiceImplTest {
    private Long project = 1L;
    private String name = "Name";
    private Date start = new Date();
    private Date stop = new Date();

    private Locale locale = Locale.ENGLISH;

    ReleaseRequestDto actual = new ReleaseRequestDto();
    @Autowired
    private MessageSource messageSource;

    private ReleaseValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        actual.setName(name);
        actual.setProject(project);
        actual.setStartDate(start);
        actual.setStopDate(stop);
        validatorService = new ReleaseValidatorServiceImpl(messageSource);
    }

    @Test
    void validateNotNull() {
        ReleaseEntity expected = validatorService.validate(actual, new ReleaseEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(expected.getProject()),
                () -> assertEquals(expected.getProject().getId(), actual.getProject()),
                () -> assertEquals(expected.getStartDate(), actual.getStopDate()),
                () -> assertEquals(expected.getStopDate(), actual.getStopDate())
        );
    }

    @Test
    void validateName() {
        actual.setName(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }

    @Test
    void validateProject() {
        actual.setProject(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }

    @Test
    void validateStartDate() {
        actual.setStartDate(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }

    @Test
    void validateStopDate() {
        actual.setStopDate(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }
}