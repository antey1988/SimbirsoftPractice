package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.domain.Role;
import com.example.SimbirsoftPractice.rest.dto.UserRequestDto;
import com.example.SimbirsoftPractice.rest.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "Создание, изменение, удаление пользователей системы")
public class UserController {

    @GetMapping
    @Operation(summary = "Список пользователей")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> list = new ArrayList<>();
        list.add(new UserResponseDto(1L, "Name1", new ArrayList<>()));
        list.add(new UserResponseDto(2L, "Name2", new ArrayList<>()));
        list.add(new UserResponseDto(3L, "Name3", new ArrayList<>()));
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    @Operation(summary = "Создание пользователя")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ALL);
        return ResponseEntity.ok()
                .body(new UserResponseDto(1L, requestDto.getName(), roles));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение пользователя")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto requestDto,
                                                      @PathVariable Long id) {
        List<Role> roles = new ArrayList<>();
        return ResponseEntity.ok()
                .body(new UserResponseDto(id, requestDto.getName(), roles));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
