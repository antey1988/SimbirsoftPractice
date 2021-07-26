package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    @Mapping(target = "customer", source = "customer.id")
    ProjectResponseDto entityToResponseDto(ProjectEntity projectEntity);
    @Mapping(target = "customer.id", source = "customer")
    ProjectEntity requestDtoToEntity(ProjectRequestDto projectRequestDto,
                                     @MappingTarget ProjectEntity projectEntity);
}
