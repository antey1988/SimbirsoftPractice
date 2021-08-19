package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.mappers.UserMapper;
import com.example.SimbirsoftPractice.repos.UserRepository;
import com.example.SimbirsoftPractice.rest.controllers.exceptions.NotFoundException;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import com.example.SimbirsoftPractice.services.UserService;
import com.example.SimbirsoftPractice.services.validators.UserValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository repository;
    private final UserValidatorService validator;
    private final UserMapper mapper;
    private final MessageSource messageSource;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserValidatorService validator,
                           UserMapper userMapper, MessageSource messageSource) {
        this.repository = userRepository;
        this.validator = validator;
        this.mapper = userMapper;
        this.messageSource = messageSource;
    }

    @Override
    public UserResponseDto readUser(Long id, Locale locale) {
        UserEntity userEntity = getOrElseThrow(id, locale);
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto, Locale locale) {
        UserEntity userEntity = validator.validate(userRequestDto, new UserEntity(), locale);
        userEntity = repository.save(userEntity);
        logger.info("New User added to DB");
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long id, Locale locale) {
        UserEntity userEntity = getOrElseThrow(id, locale);
        userEntity = validator.validate(userRequestDto, userEntity, locale);
        logger.info("User updated in the DB");
        return mapper.entityToResponseDto(userEntity);
    }

    @Override
    public void deleteUser(Long id, Locale locale) {
        getOrElseThrow(id, locale);
        repository.deleteById(id);
        logger.info("User deleted from DB");
    }

    @Override
    public List<UserResponseDto> getListUsers() {
        List<UserEntity> list = repository.findAll();
        logger.info("All Users retrieved from the DB");
        return mapper.listEntityToListResponseDto(list);
    }

    private UserEntity getOrElseThrow(Long id, Locale locale ) {
        logger.info(String.format("Extracting User with identifier(id) = %d from DB", id));
        Optional<UserEntity> optional = repository.findById(id);
        return optional.orElseThrow(() -> {
            logger.error(String.format("User with identifier (id) =% d does not exist", id));
            String error = messageSource.getMessage("error.NotFound", null, locale);
            String record = messageSource.getMessage("record.User", null, locale);
            return new NotFoundException(String.format(error, record, id));
        });
    }
}
