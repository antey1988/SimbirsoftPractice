package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.utils.UtilReleases;
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
        ReleaseResponseDto expected = response;
        ReleaseResponseDto actual = releaseMapper.entityToResponseDto(entity);
        assertEquals(expected, actual);
    }

    @Test
    void listEntityToListResponseDto() {
        List<ReleaseResponseDto> expected = List.of(response, response, response);
        List<ReleaseResponseDto> actual = releaseMapper.listEntityToListResponseDto(List.of(entity, entity, entity));
        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }
}