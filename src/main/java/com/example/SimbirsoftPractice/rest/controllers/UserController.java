package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import com.example.SimbirsoftPractice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Создание, изменение, удаление пользователей системы")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Список пользователей")
    public ResponseEntity<List<UserResponseDto>> getListUsers() {
        logger.info("Выполнен запрос на получение списка пользователей");
        List<UserResponseDto> list = service.getListUsers();
        logger.info("Список пользователей получен");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Информация о пользователе")
    public ResponseEntity<UserResponseDto> readUser(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на получение информации о пользователе c id = %d", id));
        UserResponseDto userResponseDto = service.readUser(id);
        logger.info(String.format("Информация о пользователе с id = %d получена", id));
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PostMapping
    @Operation(summary = "Создание нового пользователя")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        logger.info("Выполнен запрос на создание нового пользователя");
        UserResponseDto userResponseDto = service.createUser(requestDto);
        logger.info("Новый пользователь создан");
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение данных о пользователе")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto requestDto,
                                                      @PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на изменение информации о пользователе c id = %d", id));
        UserResponseDto userResponseDto = service.updateUser(requestDto, id);
        logger.info(String.format("Информация о пользователе c id = %d успешно обновлена", id));
        return ResponseEntity.ok().body(userResponseDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на удаление пользователя c id = %d", id));
        service.deleteUser(id);
        logger.info(String.format("Клиент c id = %d успешно удален", id));
        return ResponseEntity.accepted().build();
    }
}
