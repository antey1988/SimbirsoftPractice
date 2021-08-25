package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.configurations.MessageSourceConfig;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = MessageSourceConfig.class)
class UserValidatorServiceImplTest {
    private String name = "Name";
    private String password = "Password";
    private Locale locale = Locale.ENGLISH;

    private UserRequestDto actual;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MessageSource messageSource;

    private UserValidatorServiceImpl validatorService;

    @BeforeEach
    public void setUp() {
        validatorService = new UserValidatorServiceImpl(messageSource, passwordEncoder);
        actual = new UserRequestDto();
        actual.setName(name);
        actual.setPassword(password);
        actual.setRoles(Set.of(Role.ROLE_CRUD_USERS, Role.ROLE_CRUD_OTHERS));
    }


    @Test
    void validateNotNull() {
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        UserEntity expected = validatorService.validate(actual, new UserEntity(), locale);
        assertAll(
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getPassword(), actual.getPassword()),
                () -> assertEquals(expected.getRoles().size(), actual.getRoles().size())
        );
    }

    @Test
    void validateNullName() {
        actual.setName(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new UserEntity(), locale));
    }

    @Test
    void validateNullPassword() {
        actual.setPassword(null);
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new UserEntity(), locale));
    }
}