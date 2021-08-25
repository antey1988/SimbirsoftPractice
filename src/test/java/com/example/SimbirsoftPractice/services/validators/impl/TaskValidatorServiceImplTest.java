package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.configurations.MessageSourceConfig;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.domain.exceptions.IllegalStatusException;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
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

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = MessageSourceConfig.class)
class TaskValidatorServiceImplTest {
    private Locale locale = Locale.ENGLISH;
    private Long id = 1L;
    private String name = "Name";
    private String description = "Description";
    private StatusTask statusTask = StatusTask.IN_PROGRESS;
    private int border = 0;

    private TaskRequestDto actual = new TaskRequestDto();

    @Autowired
    private MessageSource messageSource;
    @Mock
    private ProjectRepository projectRepository;

    private TaskValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        validatorService = new TaskValidatorServiceImpl(projectRepository, messageSource);

        actual.setName(name);
        actual.setDescription(description);
        actual.setRelease(id);
        actual.setCreator(id);
        actual.setExecutor(id);
        actual.setStatus(statusTask);
        actual.setBorder(border);
    }

    @Test
    void validateNullName() {
        actual.setName(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));
    }

    @Test
    void validateNullRelease() {
        actual.setRelease(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));
    }

    @Test
    void validateNullCreator() {
        actual.setCreator(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));
    }

    @Test
    void validateNullExecutor() {
        actual.setExecutor(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));
    }

    @Test
    void validateDefaultValueStatus() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StatusProject.CREATED);
        actual.setStatus(null);
        Mockito.when(projectRepository.getProjectByRelease(actual.getRelease())).thenReturn(project);
        TaskEntity expected = validatorService.validate(actual, new TaskEntity(), locale);
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    void validateCreationWithClosedProject() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StatusProject.CLOSED);
        Mockito.when(projectRepository.getProjectByRelease(actual.getRelease())).thenReturn(project);
        assertThrows(IllegalStatusException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));

    }

    @Test
    void validateUpdateWithNotOpenProject() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StatusProject.CREATED);
        Mockito.when(projectRepository.getProjectByRelease(actual.getRelease())).thenReturn(project);
        assertThrows(IllegalStatusException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));

    }

    @Test
    void validateNotNull() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StatusProject.OPEN);
        Mockito.when(projectRepository.getProjectByRelease(actual.getRelease())).thenReturn(project);
        TaskEntity expected = validatorService.validate(actual, new TaskEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertNotNull(expected.getRelease()),
                () -> assertEquals(expected.getRelease().getId(), actual.getRelease()),
                () -> assertNotNull(expected.getCreator()),
                () -> assertEquals(expected.getCreator().getId(), actual.getCreator()),
                () -> assertNotNull(expected.getRelease()),
                () -> assertEquals(expected.getExecutor().getId(), actual.getExecutor()),
                () -> assertEquals(expected.getBorder(), actual.getBorder())
        );
    }
}