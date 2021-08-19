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
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Создание, изменение, удаление пользователей системы")
public class UserController {
    private static final String REQUEST = "Request: %s " +
            "http://localhost:8080/api/users" + "%s";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Список пользователей")
    public ResponseEntity<List<UserResponseDto>> getListUsers() {
        logger.info(String.format(REQUEST, "GET", ""));
        List<UserResponseDto> list = service.getListUsers();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Информация о пользователе")
    public ResponseEntity<UserResponseDto> readUser(@PathVariable Long id, Locale locale) {
        logger.info(String.format(REQUEST, "GET", "/" + id));
        UserResponseDto userResponseDto = service.readUser(id, locale);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PostMapping
    @Operation(summary = "Создание нового пользователя")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto, Locale locale) {
        logger.info(String.format(REQUEST, "POST", ""));
        UserResponseDto userResponseDto = service.createUser(requestDto, locale);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение данных о пользователе")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto requestDto,
                                                      @PathVariable Long id, Locale locale) {
        logger.info(String.format(REQUEST, "PUT", "/" + id));
        UserResponseDto userResponseDto = service.updateUser(requestDto, id, locale);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Locale locale) {
        logger.info(String.format(REQUEST, "DELETE", "/" + id));
        service.deleteUser(id, locale);
        return ResponseEntity.accepted().build();
    }
}
