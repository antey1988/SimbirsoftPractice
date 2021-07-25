package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponseDto entityToResponseDto(CustomerEntity customerEntity);
    List<CustomerResponseDto> listEntitiesToListResponseDto(List<CustomerEntity> listCustomerEntities);
    CustomerEntity RequestDtoToEntity(CustomerRequestDto customerRequestDto);
}
