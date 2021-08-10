package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Проекты", description = "Создание, изменение, удаление, просмотр списка проектов")
public class ProjectController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Создание нового проекта")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto requestDto) {
        logger.info("Выполнен запрос на создание нового клиента");
        ProjectResponseDto projectResponseDto = service.createProject(requestDto);
        logger.info("Новый клиент создан");
        return ResponseEntity.ok().body(projectResponseDto);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Просмотр информации о проекте")
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на получение информации о проекте c id = %d", id));
        ProjectResponseDto projectResponseDto = service.readProject(id);
        logger.info(String.format("Информация о проекте c id = %d поучена", id));
        return ResponseEntity.ok().body(projectResponseDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение информации о проекте")
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestBody ProjectRequestDto requestDto,
                                                      @PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на изменение информации о проекте c id = %d", id));
        ProjectResponseDto projectResponseDto = service.updateProject(requestDto, id);
        logger.info(String.format("Информация о проекте c id = %d успешно обновлена", id));
        return ResponseEntity.ok().body(projectResponseDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление проекта")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        logger.info(String.format("Выполнен запрос на удаление проекта c id = %d", id));
        service.deleteProject(id);
        logger.info(String.format("Проект c id = %d успешно удален", id));
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    @Operation(summary = "Просмотр списка проектов")
    public ResponseEntity<List<ProjectResponseDto>> readListProjects(@RequestParam(required = false) Long id) {
        logger.info("Выполнен запрос на получение списка проектов");
        List<ProjectResponseDto> list = service.readListProjects(id);
        logger.info("Список проектов получен");
        return ResponseEntity.ok().body(list);
    }

}
