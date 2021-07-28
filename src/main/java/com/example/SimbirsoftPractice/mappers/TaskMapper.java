package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.ReleaseEntity;
import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mappings({
            @Mapping(target = "creator", expression = "java(taskEntity.getCreator().getId())"),
            @Mapping(target = "executor", expression = "java(taskEntity.getExecutor().getId())"),
            @Mapping(target = "release", expression = "java(taskEntity.getRelease().getId())")
    })
    TaskResponseDto entityToResponseDto(TaskEntity taskEntity);

    List<TaskResponseDto> listEntityToListResponseDto(List<TaskEntity> entityList);

    @Mappings({
            @Mapping(target = "creator", source = "creator", qualifiedByName = "getNewUserWithId"),
            @Mapping(target = "executor", source = "executor", qualifiedByName = "getNewUserWithId"),
            @Mapping(target = "release", source = "release", qualifiedByName = "getNewReleaseWithId")
    })
    TaskEntity requestDtoToEntity(TaskRequestDto requestDto, @MappingTarget TaskEntity entity);

    @Named("getNewUserWithId")
    default UserEntity getNewUserWithId(Long user) {
        UserEntity entity = new UserEntity();
        entity.setId(user);
        return entity;
    }

    @Named("getNewReleaseWithId")
    default ReleaseEntity getNewReleaseWithId(Long release) {
        ReleaseEntity entity = new ReleaseEntity();
        entity.setId(release);
        return entity;
    }
}
