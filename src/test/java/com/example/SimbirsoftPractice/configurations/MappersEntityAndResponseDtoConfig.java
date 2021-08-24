package com.example.SimbirsoftPractice.configurations;

import com.example.SimbirsoftPractice.entities.*;
import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.*;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class MappersEntityAndResponseDtoConfig {
    Long id = 1L;
    String name = "Name";
    String password = "Password";
    String description = "Description";
    UUID uuid = UUID.randomUUID();
    Set<Role> roles = Set.of(Role.ROLE_CRUD_USERS, Role.ROLE_CRUD_OTHERS);
    Date start = new Date();
    Date stop = new Date();
    StatusProject statusProject = StatusProject.CREATED;
    StatusTask statusTask = StatusTask.BACKLOG;
    BigDecimal price = new BigDecimal("10.00");
    int border = 0;

    @Bean
    public CustomerEntity customerEntity() {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setUuid(uuid);
        return entity;
    }
    @Bean
    public CustomerResponseDto customerResponseDto() {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setId(id);
        response.setName(name);
        return response;
    }

    @Bean
    public UserEntity userEntity() {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setPassword(password);
        entity.setRoles(roles);
        return entity;
    }
    @Bean
    public UserResponseDto userResponseDto() {
        UserResponseDto response = new UserResponseDto();
        response.setId(id);
        response.setName(name);
        response.setRoles(roles);
        return  response;
    }

    @Bean
    public ProjectEntity projectEntity() {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setCustomer(customerEntity());
        entity.setStatus(statusProject);
        entity.setStartDate(start);
        entity.setStopDate(stop);
        entity.setPrice(price);
        return entity;
    }
    @Bean
    public ProjectResponseDto projectResponseDto() {
        ProjectResponseDto response = new ProjectResponseDto();
        response.setId(id);
        response.setName(name);
        response.setDescription(description);
        response.setCustomer(customerEntity().getId());
        response.setStatus(statusProject);
        response.setStartDate(start);
        response.setStopDate(stop);
        response.setPrice(price);
        return response;
    }
    @Bean
    public PaymentProjectRequestDto paymentProjectRequestDto() {
        PaymentProjectRequestDto payment = new PaymentProjectRequestDto();
        payment.setName(name);
        payment.setUuid(uuid);
        payment.setPrice(price);
        return payment;
    }

    @Bean
    public ReleaseEntity releaseEntity() {
        ReleaseEntity entity = new ReleaseEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setStartDate(start);
        entity.setStopDate(stop);
        entity.setProject(projectEntity());
        return entity;
    }
    @Bean
    public ReleaseResponseDto releaseResponseDto() {
        ReleaseResponseDto response = new ReleaseResponseDto();
        response.setId(id);
        response.setName(name);
        response.setStartDate(start);
        response.setStopDate(stop);
        response.setProject(projectEntity().getId());
        return  response;
    }

    @Bean
    public TaskEntity taskEntity() {
        TaskEntity entity = new TaskEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setRelease(releaseEntity());
        entity.setCreator(userEntity());
        entity.setExecutor(userEntity());
        entity.setStatus(statusTask);
        entity.setBorder(border);
        return entity;
    }
    @Bean
    public TaskResponseDto taskResponseDto() {
        TaskResponseDto response = new TaskResponseDto();
        response.setId(id);
        response.setName(name);
        response.setDescription(description);
        response.setRelease(releaseEntity().getId());
        response.setCreator(userEntity().getId());
        response.setExecutor(userEntity().getId());
        response.setStatus(statusTask);
        response.setBorder(border);
        return response;
    }
}
