package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = CustomerMapperImpl.class)
class CustomerMapperImplTest {
    private static CustomerEntity entity;
    private static CustomerResponseDto response;
    @Autowired
    private CustomerMapperImpl customerMapper;

    @BeforeAll
    static void setUp() {
        Long id = 1L;
        String name = "Customer";
        UUID uuid = UUID.randomUUID();

        entity = new CustomerEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setUuid(uuid);

        response = new CustomerResponseDto();
        response.setId(id);
        response.setName(name);
    }

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