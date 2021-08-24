package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.MappersEntityAndResponseDtoConfig;
import com.example.SimbirsoftPractice.configurations.MappersConfig;
import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MappersConfig.class, MappersEntityAndResponseDtoConfig.class})
class ReleaseMapperImplTest {
    @Autowired
    private ReleaseEntity entity;
    @Autowired
    private ReleaseResponseDto response;
    @Autowired
    private ReleaseMapperImpl releaseMapper;

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