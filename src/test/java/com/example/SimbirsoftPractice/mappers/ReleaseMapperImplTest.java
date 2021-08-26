package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.UtilReleases;
import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ReleaseMapperImplTest {
    private final ReleaseEntity entity = UtilReleases.defaultEntity();
    private final ReleaseResponseDto response = UtilReleases.defaultResponse();

    private ReleaseMapperImpl releaseMapper = new ReleaseMapperImpl();

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