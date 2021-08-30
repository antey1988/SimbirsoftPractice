package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.utils.UtilCustomers;
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
        CustomerResponseDto expected = response;
        CustomerResponseDto actual = customerMapper.entityToResponseDto(entity);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void listEntityToListResponseDto() {
        List<CustomerResponseDto> expected = List.of(response, response);
        List<CustomerResponseDto> actual = customerMapper.listEntityToListResponseDto(List.of(entity, entity));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void entityToResponseDtoWithUUID() {
        CustomerEntity expected = entity;
        CustomerWithUUIDRequestDto actual = customerMapper.entityToResponseDtoWithUUID(entity);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getUuid(), actual.getUuid());
    }
}