package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.mappers.ProjectMapper;
import com.example.SimbirsoftPractice.repos.ProjectRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.PaymentService;
import com.example.SimbirsoftPractice.services.validators.ProjectValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {
    private final Long id_1 = 1L;
    private final Long id_2 = 2L;

    @Mock
    ProjectRepository repository;
    @Mock
    ProjectMapper mapper;
    @Mock
    ProjectValidatorService validator;
    @Mock
    PaymentService paymentService;
    @Mock
    MessageSource messageSource;

    @InjectMocks
    ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(repository.findById(id_1)).thenReturn(Optional.of(new ProjectEntity()));
        Mockito.lenient().when(mapper.entityToResponseDto(Mockito.any())).thenReturn(new ProjectResponseDto());
    }

    @Test
    void readProject() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertNotNull(projectService.readProject(id_1, null));

        Mockito.when(repository.findById(id_2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> projectService.readProject(id_2, null));
    }

    @Test
    void createProject() {
        ProjectEntity entity = new ProjectEntity();
        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);

        assertNotNull(projectService.createProject(new ProjectRequestDto(), null));
    }

    @Test
    void updateProject() {
        ProjectEntity entity = new ProjectEntity();
        entity.setStatus(StatusProject.OPEN);
        PaymentProjectRequestDto request = new PaymentProjectRequestDto();

        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);
        Mockito.when(mapper.entityToPaymentProjectRequestDto(entity)).thenReturn(request);
        Mockito.doNothing().when(paymentService).payProject(request, null);

        assertNotNull(projectService.updateProject(new ProjectRequestDto(), id_1, null));
    }

    @Test
    void deleteProject() {
        Mockito.doNothing().when(repository).deleteById(id_1);

        projectService.deleteProject(id_1, null);
        Mockito.verify(repository).deleteById(id_1);
    }

    @Test
    void getListProjects() {
        List<ProjectEntity> list = List.of(new ProjectEntity(), new ProjectEntity());
        Mockito.when(mapper.listEntityToListResponseDto(list))
                .thenReturn(List.of(new ProjectResponseDto(), new ProjectResponseDto()));

        Mockito.when(repository.findAll()).thenReturn(list);

        List<ProjectResponseDto> actual = projectService.readListProjects(null);
        assertNotNull(actual);
        assertEquals(actual.size(), 2);


        Mockito.when(repository.findByCustomerId(id_1)).thenReturn(list);

        actual = projectService.readListProjects(id_1);
        assertNotNull(actual);
        assertEquals(actual.size(), 2);
    }
}