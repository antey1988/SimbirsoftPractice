package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.domain.StatusProject;
import com.example.SimbirsoftPractice.rest.dto.CustomerResponseDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ProjectResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Проекты", description = "Создание, изменение, удаление, просмотр списка проектов")
public class ProjectController {

    @GetMapping
    @Operation(summary = "Список проектов")
    public ResponseEntity<List<ProjectResponseDto>> getProjects() {
        List<ProjectResponseDto> list = new ArrayList<>();
        list.add(new ProjectResponseDto(1L, "Name1", "Desc1", new CustomerResponseDto(1L, "Customer1"), new Date(), new Date(), StatusProject.CLOSED));
        list.add(new ProjectResponseDto(2L, "Name2", "Desc2", new CustomerResponseDto(1L, "Customer1"), new Date(), new Date(), StatusProject.OPEN));
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @Operation(summary = "Создание проекта")
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody ProjectRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(new ProjectResponseDto(3L, requestDto.getName(), requestDto.getDescription(), new CustomerResponseDto(1L, "Name1"),
                        requestDto.getStartDate(), requestDto.getStopDate(), requestDto.getStatus()));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение проекта")
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestBody ProjectRequestDto requestDto,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok()
                .body(new ProjectResponseDto(id, requestDto.getName(), requestDto.getDescription(),  new CustomerResponseDto(1L, "Name1"),
                        requestDto.getStartDate(), requestDto.getStopDate(), requestDto.getStatus()));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление проекта")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) throws FileNotFoundException {
        throw new FileNotFoundException();
    }
}
