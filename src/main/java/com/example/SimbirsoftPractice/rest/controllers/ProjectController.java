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
    private static final String REQUEST = "Request: %s " +
            "http://localhost:8080/api/pojects" + "%s";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Создание нового проекта")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto requestDto) {
        logger.info(String.format(REQUEST, "POST", ""));
        ProjectResponseDto projectResponseDto = service.createProject(requestDto);
        return ResponseEntity.ok().body(projectResponseDto);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Просмотр информации о проекте")
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable Long id) {
        logger.info(String.format(REQUEST, "GET", "/" + id));
        ProjectResponseDto projectResponseDto = service.readProject(id);
        return ResponseEntity.ok().body(projectResponseDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение информации о проекте")
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestBody ProjectRequestDto requestDto,
                                                      @PathVariable Long id) {
        logger.info(String.format(REQUEST, "PUT", "/" + id));
        ProjectResponseDto projectResponseDto = service.updateProject(requestDto, id);
        return ResponseEntity.ok().body(projectResponseDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление проекта")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        logger.info(String.format(REQUEST, "DELETE", "/" + id));
        service.deleteProject(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    @Operation(summary = "Просмотр списка проектов")
    public ResponseEntity<List<ProjectResponseDto>> readListProjects(@RequestParam(required = false) Long id) {
        logger.info(String.format(REQUEST, "GET", ""));
        List<ProjectResponseDto> list = service.readListProjects(id);
        return ResponseEntity.ok().body(list);
    }

}
