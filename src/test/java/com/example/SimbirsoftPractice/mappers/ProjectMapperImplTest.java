package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.UtilProjects;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ProjectMapperImplTest {
    private final ProjectEntity entity = UtilProjects.defaultEntity();
    private final ProjectResponseDto response = UtilProjects.defaultResponse();
    private final PaymentProjectRequestDto payment = UtilProjects.defaultRequestPayment();

    private ProjectMapperImpl projectMapper = new ProjectMapperImpl();

    @Test
    void entityToResponseDto() {
        ProjectResponseDto actual = response;
        ProjectResponseDto expected = projectMapper.entityToResponseDto(entity);
        assertEquals(expected, actual);
    }

    @Test
    void listEntityToListResponseDto() {
        List<ProjectResponseDto> actual = List.of(response, response, response);
        List<ProjectResponseDto> expected = projectMapper.listEntityToListResponseDto(List.of(entity, entity, entity));
        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void entityToPaymentProjectRequestDto() {
        PaymentProjectRequestDto actual = payment;
        PaymentProjectRequestDto expected = projectMapper.entityToPaymentProjectRequestDto(entity);
        assertEquals(expected, actual);
    }
}