package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.dto.PaymentProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = ProjectMapperImpl.class)
class ProjectMapperImplTest {
    private static ProjectEntity entity = new ProjectEntity();
    private static ProjectResponseDto response = new ProjectResponseDto();
    private static PaymentProjectRequestDto payment = new PaymentProjectRequestDto();
    @Autowired
    private ProjectMapperImpl projectMapper;

    @BeforeAll
    static void setUp() {
        Long id = 1L;
        String name = "Project";
        String description = "Description";
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        UUID uuid = UUID.randomUUID();
        customer.setUuid(uuid);
        Date start = new Date();
        Date stop = new Date();
        StatusProject status = StatusProject.CREATED;
        BigDecimal price = new BigDecimal("10.00");

        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setCustomer(customer);
        entity.setStatus(status);
        entity.setStartDate(start);
        entity.setStopDate(stop);
        entity.setPrice(price);

        response.setId(id);
        response.setName(name);
        response.setDescription(description);
        response.setCustomer(customer.getId());
        response.setStatus(status);
        response.setStartDate(start);
        response.setStopDate(stop);
        response.setPrice(price);

        payment.setName(name);
        payment.setUuid(uuid);
        payment.setPrice(price);
    }

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