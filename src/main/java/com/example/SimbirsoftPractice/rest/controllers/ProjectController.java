package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import com.example.SimbirsoftPractice.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Проекты", description = "Создание, изменение, удаление, просмотр списка проектов")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Создание нового проекта")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto requestDto) {
        return ResponseEntity.ok().body(service.createProject(requestDto));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Просмотр информации о проекте")
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.readProject(id));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение информации о проекте")
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestBody ProjectRequestDto requestDto,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok().body(service.updateProject(requestDto, id));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление проекта")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        service.deleteProject(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    @Operation(summary = "Просмотр списка проектов")
    public ResponseEntity<List<ProjectResponseDto>> readListProjects(@RequestParam(required = false) Long id) {
        List<ProjectResponseDto> list = service.readListProjects(id);
        return ResponseEntity.ok().body(list);
    }

}
