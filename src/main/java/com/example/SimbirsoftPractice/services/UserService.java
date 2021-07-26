package com.example.SimbirsoftPractice.services;

import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto getUser(Long id);
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto updateUser(UserRequestDto userRequestDto, Long id);
    void deleteUser(Long id);
    List<UserResponseDto> getListUsers();
}
