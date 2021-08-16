package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.mappers.UserMapper;
import com.example.SimbirsoftPractice.repos.UserRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import com.example.SimbirsoftPractice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder encoder) {
        this.repository = userRepository;
        this.mapper = userMapper;
        this.encoder = encoder;
    }

    @Override
    public UserResponseDto readUser(Long id) {
        UserEntity userEntity = getOrElseThrow(id);
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(encoder.encode(userRequestDto.getPassword()));
        UserEntity userEntity = mapper.requestDtoToEntity(userRequestDto, new UserEntity());
        userEntity = repository.save(userEntity);
        logger.info("Новая запись добавлена в базу данных");
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long id) {
        UserEntity userEntity = getOrElseThrow(id);
        userEntity = mapper.requestDtoToEntity(userRequestDto, userEntity);
        logger.info("Запись обновлена в базе данных");
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity userEntity = getOrElseThrow(id);
        repository.deleteById(id);
        logger.info("Запись удалена из базы данных");
    }

    @Override
    public List<UserResponseDto> getListUsers() {
        List<UserEntity> list = repository.findAll();
        logger.info("Список записей извлечен из базы данных");
        return mapper.listEntityToListResponseDto(list);
    }

    private UserEntity getOrElseThrow(Long id) {
        logger.info(String.format("Попытка извлечения записи c id = %d из базы данных", id));
        Optional<UserEntity> userOptional = repository.findById(id);
        UserEntity entity = userOptional.orElseThrow(() -> {
            logger.warn(String.format("Запись c id = %d отсутсвует в базе данных", id));
            return new NotFoundException(String.format("Пользователь с id = %d не существует", id));
        });
        logger.info(String.format("Запись c id = %d успешно извлечена из базы данных", id));
        return entity;
    }
}
