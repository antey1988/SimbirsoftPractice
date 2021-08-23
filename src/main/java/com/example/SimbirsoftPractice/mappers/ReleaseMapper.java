package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReleaseMapper {
    @Mapping(target = "project", source = "project.id")
    ReleaseResponseDto entityToResponseDto(ReleaseEntity releaseEntity);

    List<ReleaseResponseDto> listEntityToListResponseDto(List<ReleaseEntity> entityList);
//
//    @Mapping(target = "project", source = "project",qualifiedByName = "newProject")
//    ReleaseEntity requestDtoToEntity(ReleaseRequestDto requestDto, @MappingTarget ReleaseEntity entity);
//
//    @Named("newProject")
//    default ProjectEntity getNewProjectWithId(Long project) {
//        ProjectEntity entity = new ProjectEntity();
//        entity.setId(project);
//        return entity;
//    }
}
