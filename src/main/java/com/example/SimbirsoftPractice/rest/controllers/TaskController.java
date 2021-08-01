package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Задачи", description = "Создание, изменение, удаление, просмотр списка задач")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping(value = "/tasks")
    @Operation(summary = "Создание новой задачи")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto responseDto = service.createTask(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/tasks/{id}")
    @Operation(summary = "Просмотр сосотояния задачи")
    public ResponseEntity<TaskResponseDto> readTask(@PathVariable Long id) {
        TaskResponseDto responseDto = service.readTask(id);
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping(value = "/tasks/{id}")
    @Operation(summary = "Изменение состояния задачи")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto requestDto,
                                                         @PathVariable Long id) {
        TaskResponseDto responseDto = service.updateTask(requestDto, id);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping(value = "/tasks/{id}")
    @Operation(summary = "Удаление задачи")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/releases/{id}/tasks")
    @Operation(summary = "Список задач с возможностью фильтрации по статусу, созданных в рамках одного релиза")
    public ResponseEntity<List<TaskResponseDto>> readListTasksOfRelease(@PathVariable Long id, @RequestParam(name = "status", required = false) List<StatusTask> statuses) {
        List<TaskResponseDto> list = service.readListTasksByReleaseId(id, statuses);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/user/{id}/tasks/creation")
    @Operation(summary = "Список всех задач, созданных одним пользователем")
    public ResponseEntity<List<TaskResponseDto>> readListTasksOfCreatorUser(@PathVariable Long id) {
        List<TaskResponseDto> list = service.readListTasksByCreatorId(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/user/{id}/tasks/execution")
    @Operation(summary = "Список всех задач, испольнителем которых является указанный пользователь")
    public ResponseEntity<List<TaskResponseDto>> readListTasksOfExecutorUser(@PathVariable Long id) {
        List<TaskResponseDto> list = service.readListTasksByExecutorId(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/tasks")
    @Operation(summary = "Список всех задач с возможностью фильтрации")
    public ResponseEntity<List<TaskResponseDto>> readListTasksByFilters(@RequestParam(name = "release", required = false) Long rId,
                                                                        @RequestParam(name = "creator", required = false) Long cId,
                                                                        @RequestParam(name = "executor", required = false) Long eId,
                                                                        @RequestParam(name = "status", required = false) List<StatusTask> statuses) {
        List<TaskResponseDto> list = service.readListAllTasksByFilters(rId, cId, eId, statuses);
        return ResponseEntity.ok().body(list);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<?> handlerException() {
        return ResponseEntity.notFound().build();
    }
}
