package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.mappers.UserMapper;
import com.example.SimbirsoftPractice.repos.UserRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import com.example.SimbirsoftPractice.services.validators.UserValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final Long id_1 = 1L;
    private final Long id_2 = 2L;

    @Mock
    UserRepository repository;
    @Mock
    UserMapper mapper;
    @Mock
    UserValidatorService validator;
    @Mock
    MessageSource messageSource;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(repository.findById(id_1)).thenReturn(Optional.of(new UserEntity()));
        Mockito.lenient().when(mapper.entityToResponseDto(Mockito.any())).thenReturn(new UserResponseDto());
    }

    @Test
    void readUser() {
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertNotNull(userService.readUser(id_1, null));

        Mockito.when(repository.findById(id_2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.readUser(id_2, null));
    }

    @Test
    void createUser() {
        UserEntity entity = new UserEntity();
        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);
        assertNotNull(userService.createUser(new UserRequestDto(), null));
    }

    @Test
    void updateUser() {
        UserEntity entity = new UserEntity();
        Mockito.when(validator.validate(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(entity);
        assertNotNull(userService.updateUser(new UserRequestDto(), id_1, null));
    }

    @Test
    void deleteUser() {
        Mockito.doNothing().when(repository).deleteById(id_1);
        userService.deleteUser(id_1, null);
        Mockito.verify(repository).deleteById(id_1);
    }

    @Test
    void getListUsers() {
        List<UserEntity> list = List.of(new UserEntity(), new UserEntity());
        Mockito.when(repository.findAll()).thenReturn(list);
        Mockito.when(mapper.listEntityToListResponseDto(list))
                .thenReturn(List.of(new UserResponseDto(), new UserResponseDto()));
        List<UserResponseDto> response = userService.getListUsers();
        assertNotNull(response);
        assertEquals(response.size(), 2);
    }
}