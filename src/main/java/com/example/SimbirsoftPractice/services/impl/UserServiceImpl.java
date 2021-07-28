package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.mappers.UserMapper;
import com.example.SimbirsoftPractice.repos.UserRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import com.example.SimbirsoftPractice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.repository = userRepository;
        this.mapper = userMapper;
    }

    @Override
    public UserResponseDto readUser(Long id) {
        UserEntity userEntity = getOrElseThrow(id);
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        UserEntity userEntity = mapper.requestDtoToEntity(userRequestDto, new UserEntity());
        userEntity = repository.save(userEntity);
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long id) {
        UserEntity userEntity = getOrElseThrow(id);
        userEntity = mapper.requestDtoToEntity(userRequestDto, userEntity);
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = getOrElseThrow(id);
        repository.deleteById(id);
    }

    @Override
    public List<UserResponseDto> getListUsers() {
        List<UserEntity> list = repository.findAll();
        return mapper.listEntityToListResponseDto(list);
    }

    private UserEntity getOrElseThrow(Long id) {
        Optional<UserEntity> userOptional = repository.findById(id);
        return userOptional.orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не существует", id)));
    }
}
