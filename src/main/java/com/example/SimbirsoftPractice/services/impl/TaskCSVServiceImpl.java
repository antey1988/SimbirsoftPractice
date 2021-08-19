package com.example.SimbirsoftPractice.services.impl;

import com.example.SimbirsoftPractice.entities.TaskEntity;
import com.example.SimbirsoftPractice.mappers.TaskMapper;
import com.example.SimbirsoftPractice.repos.TaskRepository;
import com.example.SimbirsoftPractice.rest.dto.TaskRequestDto;
import com.example.SimbirsoftPractice.security.IDable;
import com.example.SimbirsoftPractice.services.TaskCSVService;
import com.example.SimbirsoftPractice.services.validators.TaskValidatorService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
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
import java.util.*;

@Service
public class TaskCSVServiceImpl implements TaskCSVService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Path rootDirectory;
    private final TaskValidatorService validator;
    private final TaskRepository repository;
    private final MessageSource messageSource;

    public TaskCSVServiceImpl(TaskValidatorService validator, TaskRepository repository, MessageSource messageSource) {
        this.validator = validator;
        this.repository = repository;
        this.messageSource = messageSource;
        String resource = Objects.requireNonNull(this.getClass().getResource(".")).getPath();
        resource = System.getProperty("os.name").equalsIgnoreCase("windows") ? resource.substring(1) : resource;
        rootDirectory = Paths.get(resource).resolve("upload");
        initDirectory();
    }

    private void initDirectory() {
        try {
            Files.createDirectories(rootDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String saveFile(MultipartFile file, Locale locale) {
        String fileName = file.getOriginalFilename();
        try {
            Path path = rootDirectory.resolve(fileName);
            Files.deleteIfExists(path);
            Files.copy(file.getInputStream(), path);
            logger.info(String.format("File %s saved in %s directory", fileName, rootDirectory));
            return fileName;
        } catch (Exception e) {
            logger.error("Failed to save file");
            logger.error(e.getMessage());
            throw new RuntimeException(messageSource.getMessage("csv.notSaveFile", null, locale));
        }
    }

    @Override
    public String createFromCSV(String filename, Locale locale) {
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
                        entity = validator.validate(dto, new TaskEntity(), locale);
                    } catch (RuntimeException e) {
                        //пишем в лог
                        String warn = String.format("Line %d contains incorrect information: %s",
                                record.getRecordNumber(), e.getMessage());
                        logger.warn(warn);
                        //сохраняемв список для оповещения пользователя
                        warn = String.format(messageSource.getMessage("csv.notCorrectLine", null, locale),
                                record.getRecordNumber(), e.getMessage());
                        badTask.add("\n" + warn);
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
        String warn = messageSource.getMessage("csv.response", null, locale);
        return String.format(warn, newTasks.size(), badTask.size(), bad);
    }
}
