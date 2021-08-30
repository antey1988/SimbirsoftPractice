package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.utils.UtilProjects;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectValidatorServiceImplTest {
    private final Locale locale = Locale.ENGLISH;

    private  ProjectRequestDto expected;
    @Mock
    private MessageSource messageSource;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private ProjectValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        expected = UtilProjects.defaultRequest();
        Mockito.lenient().when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
    }

    @Test
    void validateNullName() {
        expected.setName(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(expected, new ProjectEntity(), locale));
    }

    @Test
    void validateNullCustomer() {
        expected.setCustomer(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(expected, new ProjectEntity(), locale));
    }

    @Test
    void validateDefaultValue() {
        expected.setPrice(null);
        expected.setStatus(null);
        ProjectEntity actual = validatorService.validate(expected, new ProjectEntity(), locale);
        assertAll(
                () -> assertEquals(new BigDecimal("0.00"), actual.getPrice()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    @Test
    void validateNotNull() {
        ProjectEntity actual = validatorService.validate(expected, new ProjectEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertNotNull(expected.getCustomer()),
                () -> assertEquals(expected.getCustomer(), actual.getCustomer().getId())
        );
    }

    @Test
    void validateStatusCreated() {
        ProjectEntity actual = validatorService.validate(expected, new ProjectEntity(), locale);
        assertAll(
                () -> assertNull(actual.getStartDate()),
                () -> assertNull(actual.getStopDate()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    @Test
    void validateStatusOpen() {
        expected.setStatus(StatusProject.OPEN);
        ProjectEntity actual = validatorService.validate(expected, new ProjectEntity(), locale);
        assertAll(
                () -> assertNotNull(actual.getStartDate()),
                () -> assertNull(actual.getStopDate()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    @Test
    void validateStatusClosed() {
        expected.setStatus(StatusProject.CLOSED);
        Mockito.when(taskRepository.countTasksInProcessByProjectId(null, StatusTask.DONE)).thenReturn(0L);
        ProjectEntity actual = validatorService.validate(expected, new ProjectEntity(), locale);
        assertAll(
                () -> assertNotNull(actual.getStartDate()),
                () -> assertNotNull(actual.getStopDate()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    @Test
    void validateHaveNotDoneTasks() {
        expected.setStatus(StatusProject.CLOSED);
        Mockito.when(taskRepository.countTasksInProcessByProjectId(null, StatusTask.DONE)).thenReturn(1L);
        assertThrows(IllegalStatusException.class, () -> validatorService.validate(expected, new ProjectEntity(), locale));
    }
}