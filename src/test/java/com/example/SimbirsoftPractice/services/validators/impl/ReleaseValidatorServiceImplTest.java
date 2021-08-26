package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalDateException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReleaseValidatorServiceImplTest {
    private final Locale locale = Locale.ENGLISH;

    ReleaseRequestDto actual = new ReleaseRequestDto();
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ReleaseValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        actual.setName("Name");
        actual.setProject(1L);
        actual.setStartDate(new Date());
        actual.setStopDate(new Date());
        Mockito.lenient().when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
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
    void validateNullName() {
        actual.setName(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }

    @Test
    void validateNullProject() {
        actual.setProject(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }

    @Test
    void validateNullStartDate() {
        actual.setStartDate(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }

    @Test
    void validateNullStopDate() {
        actual.setStopDate(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }

    @Test
    void validateStartAndStopDate() {
        actual.setStopDate(new Date(actual.getStartDate().getTime() - 1));
        assertThrows(IllegalDateException.class,
                () -> validatorService.validate(actual, new ReleaseEntity(), locale));
    }
}