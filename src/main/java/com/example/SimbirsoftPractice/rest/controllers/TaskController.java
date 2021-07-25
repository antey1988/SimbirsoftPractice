package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
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
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Задачи", description = "Создание, изменение, удаление, просмотр списка задач")
public class TaskController {

    @GetMapping
    @Operation(summary = "Список задач")
    public ResponseEntity<List<TaskResponseDto>> getTasks() {
        List<TaskResponseDto> list = new ArrayList<>();
        UserResponseDto userResponseDto = new UserResponseDto(1L, "User1");
        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        list.add(new TaskResponseDto(1L, "Name1", "Desc1", userResponseDto, projectResponseDto));
        list.add(new TaskResponseDto(1L, "Name1", "Desc1", userResponseDto, projectResponseDto));
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    @Operation(summary = "Создание задачи")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(new TaskResponseDto(3L, requestDto.getName(), requestDto.getDescription(),
                        new UserResponseDto(1L, "User1"), new ProjectResponseDto()));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение задачи")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto requestDto,
                                                         @PathVariable Long id) {
        return ResponseEntity.ok()
                .body(new TaskResponseDto(id, requestDto.getName(), requestDto.getDescription(),
                        new UserResponseDto(1L, "User1"), new ProjectResponseDto()));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление задачи")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) throws IOException {
        throw new FileNotFoundException();
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<?> handlerException() {
        return ResponseEntity.notFound().build();
    }
}
