package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.utils.UtilReleases;
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

    ReleaseRequestDto expected;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ReleaseValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        expected = UtilReleases.defaultRequest();
        Mockito.lenient().when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
    }

    @Test
    void validateNotNull() {
        ReleaseEntity actual = validatorService.validate(expected, new ReleaseEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertNotNull(actual.getProject()),
                () -> assertEquals(expected.getProject(), actual.getProject().getId()),
                () -> assertEquals(expected.getStartDate(), actual.getStopDate()),
                () -> assertEquals(expected.getStopDate(), actual.getStopDate())
        );
    }

    @Test
    void validateNullName() {
        expected.setName(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(expected, new ReleaseEntity(), locale));
    }

    @Test
    void validateNullProject() {
        expected.setProject(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(expected, new ReleaseEntity(), locale));
    }

    @Test
    void validateNullStartDate() {
        expected.setStartDate(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(expected, new ReleaseEntity(), locale));
    }

    @Test
    void validateNullStopDate() {
        expected.setStopDate(null);
        assertThrows(NullValueFieldException.class,
                () -> validatorService.validate(expected, new ReleaseEntity(), locale));
    }

    @Test
    void validateStartAndStopDate() {
        expected.setStopDate(new Date(expected.getStartDate().getTime() - 1));
        assertThrows(IllegalDateException.class,
                () -> validatorService.validate(expected, new ReleaseEntity(), locale));
    }
}