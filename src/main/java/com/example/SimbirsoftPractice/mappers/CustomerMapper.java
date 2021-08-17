package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.rest.dto.CustomerRequestDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.CustomerWithUUIDRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponseDto entityToResponseDto(CustomerEntity customerEntity);
    List<CustomerResponseDto> listEntityToListResponseDto(List<CustomerEntity> listCustomerEntity);
    CustomerEntity requestDtoToEntity(CustomerRequestDto customerRequestDto,
                                              @MappingTarget CustomerEntity customerEntity);

    CustomerWithUUIDRequestDto entityToResponseDtoWithUUID(CustomerEntity customerEntity);
}
