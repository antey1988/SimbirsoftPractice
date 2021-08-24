package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.MappersEntityAndResponseDtoConfig;
import com.example.SimbirsoftPractice.configurations.MappersConfig;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MappersConfig.class, MappersEntityAndResponseDtoConfig.class})
class ProjectMapperImplTest {
    @Autowired
    private ProjectEntity entity;
    @Autowired
    private ProjectResponseDto response;
    @Autowired
    private PaymentProjectRequestDto payment;
    @Autowired
    private ProjectMapperImpl projectMapper;

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