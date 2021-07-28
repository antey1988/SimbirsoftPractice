package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import com.example.SimbirsoftPractice.services.ReleaseService;
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

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Релизы поектов", description = "Создание, изменение, удаление релизов проектов")
public class ReleaseController {
    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @GetMapping(value = "projects/{projectId}/releases")
    @Operation(summary = "Список релизов проекта")
    public ResponseEntity<List<ReleaseResponseDto>> readListReleasesOfProject(@PathVariable Long projectId) {
        List<ReleaseResponseDto> list = releaseService.readListReleaseByProjectId(projectId);
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/releases")
    @Operation(summary = "Создание нового релиза")
    public ResponseEntity<ReleaseResponseDto> createRelease(@RequestBody ReleaseRequestDto requestDto) {
        ReleaseResponseDto responseDto = releaseService.createRelease(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/releases/{id}")
    @Operation(summary = "Информация о релизе")
    public ResponseEntity<ReleaseResponseDto> readRelease(@PathVariable Long id) {
        ReleaseResponseDto responseDto = releaseService.readRelease(id);
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping(value = "/releases/{id}")
    @Operation(summary = "Изменение релиза")
    public ResponseEntity<ReleaseResponseDto> updateRelease(@RequestBody ReleaseRequestDto requestDto,
                                                      @PathVariable Long id) {
        ReleaseResponseDto responseDto = releaseService.updateRelease(requestDto, id);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping(value = "/releases/{id}")
    @Operation(summary = "Удаление релиза")
    public ResponseEntity<?> deleteRelease(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return ResponseEntity.accepted().build();
    }
}
