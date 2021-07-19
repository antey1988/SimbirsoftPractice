package com.example.SimbirsoftPractice.rest.controllers;

import com.example.SimbirsoftPractice.rest.dto.ReleaseRequestDto;
import com.example.SimbirsoftPractice.rest.dto.ReleaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/releases")
@Tag(name = "Релизы поектов", description = "Создание, изменение, удаление релизов проектов")
public class ReleaseController {

    @GetMapping
    @Operation(summary = "Список релизов")
    public ResponseEntity<List<ReleaseResponseDto>> getReleases() {
        List<ReleaseResponseDto> list = new ArrayList<>();
        list.add(new ReleaseResponseDto(1L, "Release1", new Date(), new Date()));
        list.add(new ReleaseResponseDto(2L, "Release2", new Date(), new Date()));
        list.add(new ReleaseResponseDto(3L, "Release3", new Date(), new Date()));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Создание релиза")
    public ResponseEntity<ReleaseResponseDto> createRelease(@RequestBody ReleaseRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(new ReleaseResponseDto(1L, requestDto.getName(), new Date(), new Date()));
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Изменение релиза")
    public ResponseEntity<ReleaseResponseDto> updateRelease(@RequestBody ReleaseRequestDto requestDto,
                                                      @PathVariable Long id) {
        return ResponseEntity.ok()
                .body(new ReleaseResponseDto(id, requestDto.getName(), requestDto.getStartDate(), requestDto.getStopDate()));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление релиза")
    public ResponseEntity<?> deleteRelease(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
