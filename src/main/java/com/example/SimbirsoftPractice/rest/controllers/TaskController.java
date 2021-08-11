package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.domain.StatusTask;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.services.TaskCSVService;
import com.example.SimbirsoftPractice.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Задачи", description = "Создание, изменение, удаление, просмотр списка задач")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TaskService service;
    private final TaskCSVService csvService;

    public TaskController(TaskService service, TaskCSVService csvService) {
        this.service = service;
        this.csvService = csvService;
    }

    @PostMapping(value = "/tasks")
    @Operation(summary = "Создание новой задачи")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto requestDto) {
        logger.info("Выполнен запрос на создание новой задачи");
        TaskResponseDto responseDto = service.createTask(requestDto);
        logger.info("Новая задача создана");
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/tasks/{id}")
    @Operation(summary = "Просмотр состояния задачи")
    public ResponseEntity<TaskResponseDto> readTask(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на получение информации о задаче c id = %d", id));
        TaskResponseDto responseDto = service.readTask(id);
        logger.info(String.format("Информация о задаче с id = %d получена", id));
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping(value = "/tasks/{id}")
    @Operation(summary = "Изменение состояния задачи")
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto requestDto,
                                                         @PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на изменение информации о задаче c id = %d", id));
        TaskResponseDto responseDto = service.updateTask(requestDto, id);
        logger.info(String.format("Информация о задаче c id = %d успешно обновлена", id));
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping(value = "/tasks/{id}")
    @Operation(summary = "Удаление задачи")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на удаление задачи c id = %d", id));
        service.deleteTask(id);
        logger.info(String.format("Задача c id = %d успешно удален", id));
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/releases/{id}/tasks")
    @Operation(summary = "Список задач с возможностью фильтрации по статусу, созданных в рамках одного релиза")
    public ResponseEntity<List<TaskResponseDto>> readListTasksOfRelease(@PathVariable Long id,
                                                                        @RequestParam(name = "status", required = false) List<StatusTask> statuses) {
        logger.info("Выполнен запрос на получение списка задач в рамках указанного релиза и указанных статусов");
        List<TaskResponseDto> list = service.readListTasksByReleaseId(id, statuses);
        logger.info("Список задач получен");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/user/{id}/tasks/creation")
    @Operation(summary = "Список всех задач, созданных одним пользователем")
    public ResponseEntity<List<TaskResponseDto>> readListTasksOfCreatorUser(@PathVariable Long id) {
        logger.info("Выполнен запрос на получение списка задач, созданных указанным пользователем");
        List<TaskResponseDto> list = service.readListTasksByCreatorId(id);
        logger.info("Список задач получен");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/user/{id}/tasks/execution")
    @Operation(summary = "Список всех задач, исполнителем которых является указанный пользователь")
    public ResponseEntity<List<TaskResponseDto>> readListTasksOfExecutorUser(@PathVariable Long id) {
        logger.info("Выполнен запрос на получение списка задач, выполненных или выполняемых указанным пользователем");
        List<TaskResponseDto> list = service.readListTasksByExecutorId(id);
        logger.info("Список задач получен");
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/tasks")
    @Operation(summary = "Список всех задач с возможностью фильтрации")
    public ResponseEntity<List<TaskResponseDto>> readListTasksByFilters(@RequestParam(name = "release", required = false) Long rId,
                                                                        @RequestParam(name = "creator", required = false) Long cId,
                                                                        @RequestParam(name = "executor", required = false) Long eId,
                                                                        @RequestParam(name = "status", required = false) List<StatusTask> statuses) {
        logger.info("Выполнен запрос на получение списка задач с возможностью фильтрации");
        List<TaskResponseDto> list = service.readListAllTasksByFilters(rId, cId, eId, statuses);
        logger.info("Список задач получен");
        return ResponseEntity.ok().body(list);
    }

    @PostMapping(value = "/tasks/upload")
    @Operation(summary = "Создание новых задач из csv-файла")
    public ResponseEntity<List<TaskResponseDto>> createTasksFromCSV(@RequestParam(name = "file") MultipartFile file) {
        String filename = csvService.saveFile(file);
        List<TaskResponseDto> list = csvService.createFromCSV(filename);
        return ResponseEntity.ok().body(list);
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<?> handlerException() {
        return ResponseEntity.notFound().build();
    }
}
