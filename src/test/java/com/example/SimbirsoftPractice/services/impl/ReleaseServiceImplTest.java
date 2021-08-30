package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.mappers.ReleaseMapper;
import com.example.SimbirsoftPractice.repos.ReleaseRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import com.example.SimbirsoftPractice.services.validators.ReleaseValidatorService;
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
class ReleaseServiceImplTest {
    private final Long id_1 = 1L;
    private final Long id_2 = 2L;

    @Mock
    ReleaseRepository repository;
    @Mock
    ReleaseMapper mapper;
    @Mock
    ReleaseValidatorService validator;
    @Mock
    MessageSource messageSource;

    @InjectMocks
    ReleaseServiceImpl releaseService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(repository.findById(id_1)).thenReturn(Optional.of(new ReleaseEntity()));
        Mockito.lenient().when(mapper.entityToResponseDto(Mockito.any())).thenReturn(new ReleaseResponseDto());
    }

    @Test
    void readRelease() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertNotNull(releaseService.readRelease(id_1, null));

        Mockito.when(repository.findById(id_2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> releaseService.readRelease(id_2, null));
    }

    @Test
    void createRelease() {
        ReleaseEntity entity = new ReleaseEntity();

        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);

        assertNotNull(releaseService.createRelease(new ReleaseRequestDto(), null));
    }

    @Test
    void updateRelease() {
        ReleaseEntity entity = new ReleaseEntity();

        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);

        assertNotNull(releaseService.updateRelease(new ReleaseRequestDto(), id_1, null));
    }

    @Test
    void deleteRelease() {
        Mockito.doNothing().when(repository).deleteById(id_1);

        releaseService.deleteRelease(id_1, null);
        Mockito.verify(repository).deleteById(id_1);
    }

    @Test
    void getListRelease() {
        List<ReleaseEntity> list = List.of(new ReleaseEntity(), new ReleaseEntity());

        Mockito.when(repository.findAllByProjectId(id_1)).thenReturn(list);
        Mockito.when(mapper.listEntityToListResponseDto(list))
                .thenReturn(List.of(new ReleaseResponseDto(), new ReleaseResponseDto()));

        List<ReleaseResponseDto> actual = releaseService.readListReleaseByProjectId(id_1);
        assertNotNull(actual);
        assertEquals(actual.size(), 2);
    }
}