package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.UtilCustomers;
import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerMapperImplTest {

    private final CustomerEntity entity = UtilCustomers.defaultEntity();
    private final CustomerResponseDto response = UtilCustomers.defaultResponse();

    private CustomerMapperImpl customerMapper = new CustomerMapperImpl();

    @Test
    void entityToResponseDto() {
        CustomerResponseDto actual = response;
        CustomerResponseDto expected = customerMapper.entityToResponseDto(entity);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void listEntityToListResponseDto() {
        List<CustomerResponseDto> actual = List.of(response, response);
        List<CustomerResponseDto> expected = customerMapper.listEntityToListResponseDto(List.of(entity, entity));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void entityToResponseDtoWithUUID() {
        CustomerEntity actual = entity;
        CustomerWithUUIDRequestDto expected = customerMapper.entityToResponseDtoWithUUID(entity);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getUuid(), actual.getUuid());
    }
}