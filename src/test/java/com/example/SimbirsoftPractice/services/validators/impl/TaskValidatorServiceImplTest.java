package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.utils.UtilProjects;
import com.example.SimbirsoftPractice.utils.UtilTasks;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
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
    private final Locale locale = Locale.ENGLISH;

    private TaskRequestDto expected;
    @Mock
    private MessageSource messageSource;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskValidatorServiceImpl validatorService;

    @BeforeEach
    void setUp() {
        expected = UtilTasks.defaultRequest();
    }

    @Test
    void validateNullName() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        expected.setName(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(expected, new TaskEntity(), locale));
    }

    @Test
    void validateNullRelease() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        expected.setRelease(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(expected, new TaskEntity(), locale));
    }

    @Test
    void validateNullCreator() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        expected.setCreator(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(expected, new TaskEntity(), locale));
    }

    @Test
    void validateNullExecutor() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        expected.setExecutor(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(expected, new TaskEntity(), locale));
    }

    @Test
    void validateDefaultValueStatus() {
        expected.setStatus(null);
        Mockito.when(projectRepository.getProjectByRelease(expected.getRelease())).thenReturn(UtilProjects.defaultEntity());
        TaskEntity actual = validatorService.validate(expected, new TaskEntity(), locale);
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    void validateCreationWithClosedProject() {
        ProjectEntity project = UtilProjects.builder().status(StatusProject.CLOSED).buildEntity();
        Mockito.when(projectRepository.getProjectByRelease(expected.getRelease())).thenReturn(project);
        assertThrows(IllegalStatusException.class, () -> validatorService.validate(expected, new TaskEntity(), locale));

    }

    @Test
    void validateUpdateWithNotOpenProject() {
        Mockito.when(projectRepository.getProjectByRelease(expected.getRelease())).thenReturn(UtilProjects.defaultEntity());
        assertThrows(IllegalStatusException.class, () -> validatorService.validate(expected, new TaskEntity(), locale));

    }

    @Test
    void validateNotNull() {
        ProjectEntity project = UtilProjects.builder().status(StatusProject.OPEN).buildEntity();
        Mockito.when(projectRepository.getProjectByRelease(expected.getRelease())).thenReturn(project);
        TaskEntity actual = validatorService.validate(expected, new TaskEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertNotNull(expected.getRelease()),
                () -> assertEquals(expected.getRelease(), actual.getRelease().getId()),
                () -> assertNotNull(expected.getCreator()),
                () -> assertEquals(expected.getCreator(), actual.getCreator().getId()),
                () -> assertNotNull(expected.getRelease()),
                () -> assertEquals(expected.getExecutor(), actual.getExecutor().getId()),
                () -> assertEquals(expected.getBorder(), actual.getBorder())
        );
    }
}