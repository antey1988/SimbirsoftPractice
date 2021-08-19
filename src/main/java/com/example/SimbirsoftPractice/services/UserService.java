package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;

import java.util.List;
import java.util.Locale;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto, Locale locale);
    UserResponseDto readUser(Long id, Locale locale);
    UserResponseDto updateUser(UserRequestDto userRequestDto, Long id, Locale locale);
    void deleteUser(Long id, Locale locale);
    List<UserResponseDto> getListUsers();
}
