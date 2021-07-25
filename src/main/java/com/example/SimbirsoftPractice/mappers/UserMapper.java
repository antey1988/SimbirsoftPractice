package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto entityToResponseDto(UserEntity userEntity);
    UserEntity RequestDtoToEntity(UserRequestDto userRequestDto);
}
