package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto entityToResponseDto(UserEntity userEntity);
    List<UserResponseDto> listEntityToListResponseDto(List<UserEntity> listUserEntity);
//    UserEntity requestDtoToEntity(UserRequestDto userRequestDto, @MappingTarget UserEntity userEntity);
}
