package com.example.SimbirsoftPractice.services.validators.impl;

import com.example.SimbirsoftPractice.configurations.UtilUsers;
import com.example.SimbirsoftPractice.entities.UserEntity;
import com.example.SimbirsoftPractice.rest.domain.exceptions.NullValueFieldException;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorServiceImplTest {
    private final Locale locale = Locale.ENGLISH;

    private UserRequestDto actual;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UserValidatorServiceImpl validatorService;

    @BeforeEach
    public void setUp() {
        actual = UtilUsers.defaultRequest();
    }


    @Test
    void validateNotNull() {
        Mockito.when(passwordEncoder.encode(actual.getPassword())).thenReturn(actual.getPassword());
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
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new UserEntity(), locale));
    }

    @Test
    void validateNullPassword() {
        actual.setPassword(null);
        Mockito.when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("");
        assertThrows(NullValueFieldException.class, () -> validatorService.validate(actual, new UserEntity(), locale));
    }
}