package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.configurations.MappersEntityAndResponseDtoConfig;
import com.example.SimbirsoftPractice.configurations.MappersConfig;
import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MappersConfig.class, MappersEntityAndResponseDtoConfig.class})
class CustomerMapperImplTest {
    @Autowired
    private CustomerEntity entity;
    @Autowired
    private CustomerResponseDto response;
    @Autowired
    private CustomerMapperImpl customerMapper;

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