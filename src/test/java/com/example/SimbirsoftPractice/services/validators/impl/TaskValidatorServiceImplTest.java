package com.example.SimbirsoftPractice.services.validators.impl;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskValidatorServiceImplTest {
    private final StatusTask statusTask = StatusTask.IN_PROGRESS;
    private final Locale locale = Locale.ENGLISH;

    private final TaskRequestDto actual = new TaskRequestDto();
    @Mock
    private MessageSource messageSource;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        Long id = 1L;
        actual.setName("Name");
        actual.setDescription("Description");
        actual.setRelease(id);
        actual.setCreator(id);
        actual.setExecutor(id);
        actual.setStatus(statusTask);
        actual.setBorder(0);
    }

    @Test
    void validateNullName() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        actual.setName(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));
    }

    @Test
    void validateNullRelease() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        actual.setRelease(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));
    }

    @Test
    void validateNullCreator() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        actual.setCreator(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new TaskEntity(), locale));
    }

    @Test
    void validateNullExecutor() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
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