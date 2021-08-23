package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = ReleaseMapperImpl.class)
class ReleaseMapperImplTest {
    private static ReleaseEntity entity = new ReleaseEntity();
    private static ReleaseResponseDto response = new ReleaseResponseDto();
    @Autowired
    private ReleaseMapperImpl releaseMapper;

    @BeforeAll
    static void setUp() {
        Long id = 1L;
        String name = "Release";
        Date start = new Date();
        Date stop = new Date();
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);

        entity.setId(id);
        entity.setName(name);
        entity.setStartDate(start);
        entity.setStopDate(stop);
        entity.setProject(project);

        response.setId(id);
        response.setName(name);
        response.setStartDate(start);
        response.setStopDate(stop);
        response.setProject(1L);
    }

    @Test
    void entityToResponseDto() {
        ReleaseResponseDto actual = response;
        ReleaseResponseDto expected = releaseMapper.entityToResponseDto(entity);
        assertEquals(expected, actual);
    }

    @Test
    void listEntityToListResponseDto() {
        List<ReleaseResponseDto> actual = List.of(response, response, response);
        List<ReleaseResponseDto> expected = releaseMapper.listEntityToListResponseDto(List.of(entity, entity, entity));
        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }
}