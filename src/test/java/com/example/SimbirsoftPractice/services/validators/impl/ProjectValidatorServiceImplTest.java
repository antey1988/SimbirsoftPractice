package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.configurations.MessageSourceConfig;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class,MockitoExtension.class})
@ContextConfiguration(classes = MessageSourceConfig.class)
class ProjectValidatorServiceImplTest {
    private Long id = 1L;
    private String name = "Name";
    private String description = "Description";
    private StatusProject statusProject = StatusProject.CREATED;
    private BigDecimal price = new BigDecimal("10.00");
    private Locale locale = Locale.ENGLISH;
    ProjectRequestDto actual;
    @Autowired
    private MessageSource messageSource;
    @Mock
    private TaskRepository taskRepository;

    private ProjectValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        actual = new ProjectRequestDto();
        actual.setName(name);
        actual.setDescription(description);
        actual.setCustomer(id);
        actual.setStatus(statusProject);
        actual.setPrice(price);

        validatorService = new ProjectValidatorServiceImpl(taskRepository, messageSource);
    }

    @Test
    void validateNullName() {
        actual.setName(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new ProjectEntity(), locale));
    }

    @Test
    void validateNullCustomer() {
        actual.setCustomer(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new ProjectEntity(), locale));
    }

    @Test
    void validateDefaultValue() {
        actual.setPrice(null);
        actual.setStatus(null);
        ProjectEntity expected = validatorService.validate(actual, new ProjectEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getPrice(), new BigDecimal("0.00")),
                () -> assertEquals(expected.getStatus(), statusProject)
        );
    }

    @Test
    void validateNotNull() {
        ProjectEntity expected = validatorService.validate(actual, new ProjectEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertNotNull(expected.getCustomer()),
                () -> assertEquals(expected.getCustomer().getId(), actual.getCustomer())
        );
    }

    @Test
    void validateStatusCreated() {
        ProjectEntity expected = validatorService.validate(actual, new ProjectEntity(), locale);
        assertAll(
                () -> assertNull(expected.getStartDate()),
                () -> assertNull(expected.getStopDate()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    @Test
    void validateStatusOpen() {
        actual.setStatus(StatusProject.OPEN);
        ProjectEntity expected = validatorService.validate(actual, new ProjectEntity(), locale);
        assertAll(
                () -> assertNotNull(expected.getStartDate()),
                () -> assertNull(expected.getStopDate()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    @Test
    void validateStatusClosed() {
        actual.setStatus(StatusProject.CLOSED);
        Mockito.when(taskRepository.countTasksInProcessByProjectId(null, StatusTask.DONE)).thenReturn(0L);
        ProjectEntity expected = validatorService.validate(actual, new ProjectEntity(), locale);
        assertAll(
                () -> assertNotNull(expected.getStartDate()),
                () -> assertNotNull(expected.getStopDate()),
                () -> assertEquals(expected.getStatus(), actual.getStatus())
        );
    }

    @Test
    void validateHaveNotDoneTasks() {
        actual.setStatus(StatusProject.CLOSED);
        Mockito.when(taskRepository.countTasksInProcessByProjectId(null, StatusTask.DONE)).thenReturn(1L);
        assertThrows(IllegalStatusException.class, () -> validatorService.validate(actual, new ProjectEntity(), locale));
    }
}