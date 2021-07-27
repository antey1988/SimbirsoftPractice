package com.example.SimbirsoftPractice.mappers;

import com.example.SimbirsoftPractice.entities.CustomerEntity;
import com.example.SimbirsoftPractice.entities.ProjectEntity;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    @Mapping(target = "customer", source = "customer.id")
    ProjectResponseDto entityToResponseDto(ProjectEntity projectEntity);

    @Mapping(target = "customer", ignore = true)
    ProjectEntity requestDtoToEntity(ProjectRequestDto projectRequestDto,
                                     @MappingTarget ProjectEntity projectEntity);

    @AfterMapping
    default void setNewCustomerWithId(ProjectRequestDto projectRequestDto,
                                      @MappingTarget ProjectEntity projectEntity) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(projectRequestDto.getCustomer());
        projectEntity.setCustomer(entity);
    }
}
