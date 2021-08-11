package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.mappers.TaskMapper;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.rest.dto.TaskResponseDto;
import com.example.SimbirsoftPractice.security.IDable;
import com.example.SimbirsoftPractice.security.UserWithId;
import com.example.SimbirsoftPractice.services.TaskCSVService;
import com.example.SimbirsoftPractice.services.TaskValidatorService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskCSVServiceImpl implements TaskCSVService {

    private static final String INFO_FILE_SAVED = "Файл %s сохранен в каталоге %s";
    private static final String WARN_RECORD_NOT_CORRECT = "Строка %d содержит не корректрую информацию: %s";
    private static final String INFO_RESPONSE = "Количество созданных задач: %d\n" +
            "Не созданных задач %d:" +
            "%s";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Path rootDirectory;
//            = Paths.get(Objects.requireNonNull(this.getClass().getResource("")).getPath()).resolve("upload");
    private final TaskValidatorService validator;
    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskCSVServiceImpl(TaskValidatorService validator, TaskRepository repository, TaskMapper mapper) {
        this.validator = validator;
        this.repository = repository;
        this.mapper = mapper;
        String resource = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
        resource = resource.startsWith("/") ? resource.substring(1) : resource;
        rootDirectory = Paths.get(resource).resolve("upload");
        initDirectory();
    }

    private void initDirectory() {
        try {
            Files.createDirectories(rootDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Не возможно создать директорию для сохранения файлов");
        }
    }

    @Override
    public String saveFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            Path path = rootDirectory.resolve(fileName);
            Files.deleteIfExists(path);
            Files.copy(file.getInputStream(), path);
            logger.info(String.format(INFO_FILE_SAVED, fileName, rootDirectory));
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить файл");
        }
    }

    @Override
    public String createFromCSV(String filename) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((IDable)(authentication.getPrincipal())).getId();
        List<String> badTask = new ArrayList<>();
        List<TaskEntity> newTasks = new ArrayList<>();
        try {
            Reader reader = new FileReader(rootDirectory.resolve(filename).toString());
            CSVFormat csvFormat = CSVFormat.Builder.create()
                    .setHeader("name", "description", "release")
                    .setIgnoreHeaderCase(true)
                    .setSkipHeaderRecord(true)
                    .build();
            try (CSVParser records = csvFormat.parse(reader)) {
                TaskEntity entity;
                TaskRequestDto dto;
                for (CSVRecord record : records) {
                    dto = new TaskRequestDto();
                    try {
                        dto.setName(record.get("name"));
                        dto.setDescription(record.get("description"));
                        dto.setRelease(Long.parseLong(record.get("release")));
                        dto.setCreator(id);
                        entity = validator.validateInputValue(dto, new TaskEntity());
                    } catch (RuntimeException e) {
                        String text = String.format(WARN_RECORD_NOT_CORRECT, record.getRecordNumber(), e.getMessage());
                        logger.warn(text);
                        badTask.add("\n" + text);
                        continue;
                    }
                    newTasks.add(entity);
                }
            }
            newTasks = repository.saveAll(newTasks);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        StringBuilder bad = new StringBuilder();
        badTask.forEach(bad::append);
        return String.format(INFO_RESPONSE, newTasks.size(), badTask.size(), bad);
    }
}
